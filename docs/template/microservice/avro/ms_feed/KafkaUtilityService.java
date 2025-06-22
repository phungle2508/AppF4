package com.f4.feed.kafka.service;

import com.f4.feed.avro.EventEnvelope;
import com.f4.feed.service.dto.FeedItemDTO;
import com.f4.feed.kafka.handler.EventDispatcher;
import com.f4.feed.kafka.runner.KafkaJobRunner;
import com.f4.feed.kafka.util.AvroConverter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.MediaType;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class KafkaUtilityService {

    private static final Logger PRODUCER_LOG = LoggerFactory.getLogger("KafkaProducerHelper");
    private static final Logger CONSUMER_LOG = LoggerFactory.getLogger("KafkaConsumerHelper");
    private final KafkaJobRunner jobRunner;
    private final StreamBridge streamBridge;
    private final ObjectMapper objectMapper;
    private final RetryTemplate retryTemplate;
    private final EventDispatcher dispatcher;
    private final Map<String, SseEmitter> emitters;
    @Value("${ssh.service-name}-input.dlq")
    private String dlqTopic;

    @Value("${kafka.dlq.enabled:true}")
    private boolean dlqEnabled;

    @Autowired
    public KafkaUtilityService(StreamBridge streamBridge, KafkaJobRunner jobRunner, RetryTemplate retryTemplate,
            EventDispatcher dispatcher, Map<String, SseEmitter> emitters) {
        this.jobRunner = jobRunner;
        this.streamBridge = streamBridge;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.retryTemplate = retryTemplate;
        this.dispatcher = dispatcher;
        this.emitters = emitters;
    }

    // --- Helper for KafkaProducer ---

    public String producer_prepareAndAttemptDirectSendReelEvent(
            String eventName, UUID userId, String title, String videoUrl,
            String configuredOutputTopic,
            AtomicReference<EventEnvelope> messageToSupply, AtomicReference<String> keyToSupply) {
        try {
            // Generate a unique key per message
            EventEnvelope envelope = AvroConverter.createFeedItemEvent(eventName, userId, null, null);
            messageToSupply.set(envelope);
            String uniqueKey = UUID.randomUUID().toString();
            keyToSupply.set(uniqueKey);
            PRODUCER_LOG.info("Generated unique key for Kafka message: {}", uniqueKey);
            return uniqueKey;
        } catch (Exception e) {
            PRODUCER_LOG.error("Error delegating to KafkaUtilityService: {}", e.getMessage(), e);
            messageToSupply.set(null);
            keyToSupply.set(null);
            return null;
        }
    }

    // --- Helpers for KafkaConsumer ---

    public void submitEventJob(EventEnvelope avroMessage, String keyStr) {
        jobRunner.submitEventJob(avroMessage, keyStr, () -> consumeAndProcessMessage(avroMessage));
    }

    public void consumeAndProcessMessage(EventEnvelope avroMessage) {
        try {
            retryTemplate.execute(context -> {
                String eventName = null;
                FeedItemDTO serviceReelDTO = null;

                try {
                    eventName = avroMessage.getEventName();
                    if (eventName == null) {
                        throw new IllegalArgumentException("Event name cannot be null");
                    }

                    if (avroMessage.getPayload() != null) {
                        // Map Avro DTO to Service DTO
                        serviceReelDTO = consumer_mapAvroToServiceReelDTO(avroMessage.getPayload());

                        if (serviceReelDTO != null) {
                            // Prepare envelope for dispatcher
                            com.f4.feed.kafka.handler.EventEnvelope<JsonNode> envelopeForDispatcher = consumer_prepareEventEnvelopeForDispatcher(
                                    eventName, serviceReelDTO);
                            if (envelopeForDispatcher != null) {
                                dispatcher.dispatch(envelopeForDispatcher);
                            } else {
                                CONSUMER_LOG.warn("Envelope for dispatcher was null after preparation for event: {}",
                                        eventName);
                            }
                        } else {
                            CONSUMER_LOG.warn("Service FeedItemDTO was null after mapping for event: {}", eventName);
                        }
                    } else {
                        CONSUMER_LOG.warn("Unsupported event type or missing payload: {}", eventName);
                    }

                    // SSE dispatch
                    consumer_dispatchToSseClients(eventName, serviceReelDTO, emitters);

                    CONSUMER_LOG.info("Successfully processed Avro message for event: {}", eventName);

                    // Send notification to completion topic
                    String completionTopic = "reel-processing-complete";
                    streamBridge.send(completionTopic,
                            "Completed processing for event " + eventName + " with ID " + UUID.randomUUID());

                    return null;
                } catch (Exception e) {
                    CONSUMER_LOG.error("Error processing Avro message (event: {}, attempt {}): {}",
                            (eventName != null ? eventName : "UNKNOWN_EVENT"),
                            context.getRetryCount() + 1,
                            e.getMessage(), e);
                    throw e;
                }
            }, context -> {
                CONSUMER_LOG.error("Message processing failed after {} attempts, sending to DLQ. Event: {}",
                        context.getRetryCount(),
                        (avroMessage.getEventName() != null ? avroMessage.getEventName() : "UNKNOWN_EVENT"));
                Exception exception = (Exception) context.getLastThrowable();
                consumer_sendToDlq(avroMessage, exception, dlqTopic, dlqEnabled, avroMessage.toString());
                throw exception; // Rethrow to ensure the retry mechanism handles it
            });
        } catch (Exception e) {
            CONSUMER_LOG.error(
                    "Fatal error in message processing that couldn't be recovered with retries: {}. Sending to DLQ. Event: {}",
                    e.getMessage(),
                    (avroMessage.getEventName() != null ? avroMessage.getEventName() : "UNKNOWN_EVENT"), e);
            consumer_sendToDlq(avroMessage, e, dlqTopic, dlqEnabled, avroMessage.toString());
            throw new RuntimeException(
                    "Fatal error in message processing, sent to DLQ: " + e.getMessage(), e);
        }
    }

    // existing helper methods: consumer_mapAvroToServiceReelDTO,
    // consumer_prepareEventEnvelopeForDispatcher, consumer_dispatchToSseClients,
    // consumer_sendToDlq etc.

    public SseEmitter consumer_registerSseEmitter(String key, Map<String, SseEmitter> emittersMap) {
        CONSUMER_LOG.debug("Helper: Registering sse client for {}", key);
        SseEmitter emitter = new SseEmitter();
        emitter.onCompletion(() -> {
            CONSUMER_LOG.debug("Helper: SSE Emitter completed for key {}. Removing from map.", key);
            emittersMap.remove(key);
        });
        emittersMap.put(key, emitter);
        return emitter;
    }

    public void consumer_unregisterSseEmitter(String key, Map<String, SseEmitter> emittersMap) {
        CONSUMER_LOG.debug("Helper: Unregistering sse emitter for: {}", key);
        SseEmitter emitter = emittersMap.get(key);
        if (emitter != null) {
            emitter.complete();
        } else {
            CONSUMER_LOG.warn("Helper: No SSE emitter found for key {} to unregister.", key);
        }
    }

    public void consumer_sendToDlq(
            Object message, Throwable exception,
            String dlqTopic, boolean dlqEnabled, String originalMessageToString) {
        if (!dlqEnabled) {
            CONSUMER_LOG.info("Helper: DLQ is disabled. Not sending message for error: {}", exception.getMessage());
            return;
        }
        try {
            CONSUMER_LOG.info("Helper: Sending failed message to DLQ topic: {}", dlqTopic);

            Map<String, Object> dlqMessage = new HashMap<>();
            dlqMessage.put("originalMessage", originalMessageToString); // Use pre-converted string
            dlqMessage.put("error", exception.getMessage());
            dlqMessage.put("errorType", exception.getClass().getName());
            dlqMessage.put("timestamp", System.currentTimeMillis());

            streamBridge.send(dlqTopic, dlqMessage); // Assumes dlqTopic is a binding name or resolvable topic
            CONSUMER_LOG.info("Helper: Message sent to DLQ successfully");
        } catch (Exception e) {
            CONSUMER_LOG.error("Helper: Failed to send message to DLQ", e);
        }
    }

    public void consumer_dispatchToSseClients(String eventName, FeedItemDTO reelDTO,
            Map<String, SseEmitter> emittersMap) {
        CONSUMER_LOG.debug("Helper: Dispatching event {} to {} SSE clients using Service DTO", eventName,
                emittersMap.size());
        emittersMap.forEach((key, emitter) -> {
            try {
                emitter.send(
                        SseEmitter.event()
                                .name(eventName)
                                .data(reelDTO != null ? reelDTO : "No payload", MediaType.APPLICATION_JSON));
                CONSUMER_LOG.trace("Helper: Successfully sent event {} to SSE client with key {}", eventName, key);
            } catch (IOException | IllegalStateException ex) {
                CONSUMER_LOG.debug("Helper: Failed to send to SSE client with key {}, removing emitter. Error: {}", key,
                        ex.getMessage());
                // emitter.complete(); // Let onCompletion handler remove it
            }
        });
    }

    // consumer_mapAvroToServiceReelDTO now delegates to AvroConverter
    public FeedItemDTO consumer_mapAvroToServiceReelDTO(com.f4.feed.avro.FeedItemDTO avroPayload) {
        CONSUMER_LOG.debug(
                "KafkaUtilityService: Delegating Avro to Service DTO conversion to AvroConverter for payload: {}",
                avroPayload);
        try {
            return AvroConverter.convertToServiceFeedItemDTO(avroPayload);
        } catch (Exception e) {
            CONSUMER_LOG.error(
                    "KafkaUtilityService: Error during Avro to Service DTO conversion delegated to AvroConverter: {}",
                    e.getMessage(), e);
            // Rethrow or handle as per requirements, ensuring consistency with how consumer
            // handles it
            throw e; // Propagate the exception (e.g., RuntimeException from AvroConverter)
        }
    }

    // New helper method to prepare the EventEnvelope for the old dispatcher
    public com.f4.feed.kafka.handler.EventEnvelope<JsonNode> consumer_prepareEventEnvelopeForDispatcher(
            String eventName, FeedItemDTO serviceDto) {
        if (serviceDto == null) {
            CONSUMER_LOG.warn("Helper: ServiceDTO is null, cannot prepare EventEnvelope for dispatcher for event: {}",
                    eventName);
            // Depending on requirements, might return an envelope with null payload or
            // throw an error
            // For now, let's return an envelope that might signify no data or an issue.
            // Or, the calling method should handle the null DTO before calling this.
            return new com.f4.feed.kafka.handler.EventEnvelope<>(eventName, null);
        }
        try {
            String reelJson = objectMapper.writeValueAsString(serviceDto); // Uses objectMapper from KafkaUtilityService
            JsonNode reelJsonNode = objectMapper.readTree(reelJson);
            return new com.f4.feed.kafka.handler.EventEnvelope<>(eventName, reelJsonNode);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            CONSUMER_LOG.error("Helper: Error serializing/deserializing service DTO for dispatcher: {}", e.getMessage(),
                    e);
            throw new RuntimeException("Helper: Failed to prepare EventEnvelope for dispatcher", e);
        }
    }

    public Map<String, SseEmitter> getEmitters() {
        return emitters;
    }
}