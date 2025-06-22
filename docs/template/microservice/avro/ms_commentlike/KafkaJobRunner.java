
package com.f4.commentlike.kafka.runner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import jakarta.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

import com.f4.commentlike.avro.EventEnvelope;
import com.f4.commentlike.avro.LikeDTO;

/**
 * A job runner for managing Kafka consumer tasks.
 * Provides thread management and task execution for Kafka event processing.
 */
@Component
public class KafkaJobRunner {

    private static final Logger log = LoggerFactory.getLogger(KafkaJobRunner.class);

    private final ExecutorService executorService;
    private final StreamBridge streamBridge;

    @Value("${spring.cloud.stream.kafka.job.thread-pool-size:10}")
    private int threadPoolSize;

    @Value("${spring.cloud.stream.kafka.job.thread-name-prefix:kafka-job-}")
    private String threadNamePrefix;

    @Value("${spring.cloud.stream.kafka.job.completion-topic:job-completion}")
    private String jobCompletionTopic;

    public KafkaJobRunner(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
        this.executorService = Executors.newFixedThreadPool(10, new KafkaThreadFactory(threadNamePrefix));
        log.info("Initialized KafkaJobRunner with thread pool size: {}", threadPoolSize);
    }

    /**
     * Submit a job to be executed asynchronously
     * 
     * @param jobId     unique identifier for the job
     * @param eventName name of the event being processed
     * @param payload   payload data for processing
     * @param task      the task to execute
     */
    public void submitJob(String jobId, String eventName, LikeDTO payload, Runnable task) {
        log.debug("Submitting job {} for event {}", jobId, eventName);
        executorService.submit(() -> {
            try {
                log.debug("Starting job {} for event {}", jobId, eventName);
                task.run();
                log.debug("Completed job {} for event {}", jobId, eventName);

                // Send completion notification
                notifyJobCompletion(jobId, eventName, true, null);
            } catch (Exception e) {
                log.error("Job {} for event {} failed with error: {}", jobId, eventName, e.getMessage(), e);

                // Send failure notification
                notifyJobCompletion(jobId, eventName, false, e.getMessage());
            }
        });
    }

    /**
     * Submit a job for processing an EventEnvelope
     * 
     * @param event the Kafka event to process
     * @param task  the task to execute with the event
     */
    public void submitEventJob(EventEnvelope event, String key, Runnable task) {
        String eventName = event.getEventName();

        submitJob(key, eventName, event.getPayload(), task);
    }

    /**
     * Send job completion notification to Kafka
     */
    private void notifyJobCompletion(String jobId, String eventName, boolean success, String errorMessage) {
        try {
            JobCompletionEvent completionEvent = new JobCompletionEvent(
                    jobId,
                    eventName,
                    System.currentTimeMillis(),
                    success,
                    errorMessage);

            boolean sent = streamBridge.send(jobCompletionTopic, completionEvent);
            if (sent) {
                log.debug("Job completion notification sent for job {}", jobId);
            } else {
                log.warn("Failed to send job completion notification for job {}", jobId);
            }
        } catch (Exception e) {
            log.error("Error sending job completion notification for job {}: {}", jobId, e.getMessage());
        }
    }

    /**
     * Custom thread factory to provide meaningful thread names
     */
    private static class KafkaThreadFactory implements ThreadFactory {
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        KafkaThreadFactory(String namePrefix) {
            this.namePrefix = namePrefix;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, namePrefix + threadNumber.getAndIncrement());
            if (thread.isDaemon()) {
                thread.setDaemon(false);
            }
            if (thread.getPriority() != Thread.NORM_PRIORITY) {
                thread.setPriority(Thread.NORM_PRIORITY);
            }
            return thread;
        }
    }

    /**
     * Job completion event POJO for Kafka notifications
     */
    public static class JobCompletionEvent {
        private String jobId;
        private String eventName;
        private long timestamp;
        private boolean success;
        private String errorMessage;

        public JobCompletionEvent() {
        }

        public JobCompletionEvent(String jobId, String eventName, long timestamp, boolean success,
                String errorMessage) {
            this.jobId = jobId;
            this.eventName = eventName;
            this.timestamp = timestamp;
            this.success = success;
            this.errorMessage = errorMessage;
        }

        public String getJobId() {
            return jobId;
        }

        public void setJobId(String jobId) {
            this.jobId = jobId;
        }

        public String getEventName() {
            return eventName;
        }

        public void setEventName(String eventName) {
            this.eventName = eventName;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }

    /**
     * Shutdown hook to cleanup resources
     */
    @PreDestroy
    public void shutdown() {
        log.info("Shutting down KafkaJobRunner");
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                log.warn("Executor did not terminate in the specified time.");
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            log.error("Shutdown interrupted", e);
            Thread.currentThread().interrupt();
            executorService.shutdownNow();
        }
    }
}
