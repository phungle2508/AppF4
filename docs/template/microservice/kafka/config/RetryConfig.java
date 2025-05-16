package com.f4.reel.kafka.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.support.RetryTemplate;

/**
 * Configuration for adding custom retry logging while using settings from
 * consul-config-dev.yml.
 */
@Configuration
@EnableRetry
public class RetryConfig {

    private static final Logger log = LoggerFactory.getLogger(RetryConfig.class);

    /**
     * Custom RetryListener to log each retry attempt.
     */
    @Bean
    public RetryListener loggingRetryListener() {
        return new RetryListener() {
            @Override
            public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback,
                    Throwable throwable) {
                log.warn("Retry attempt {} failed with exception: {}",
                        context.getRetryCount(), throwable.getMessage());

                int maxAttempts = context.getAttribute("maxAttempts") != null
                        ? (int) context.getAttribute("maxAttempts")
                        : 3;

                if (context.getRetryCount() < maxAttempts - 1) {
                    log.info("Will retry in a moment... (attempt {}/{})",
                            context.getRetryCount() + 1, maxAttempts);
                } else {
                    log.error("Maximum retry attempts reached. Giving up and sending to DLQ.");
                }
            }

            @Override
            public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
                if (context.getRetryCount() == 0) {
                    log.info("Starting message processing");
                } else {
                    log.info("Retrying operation after failure (retry {})", context.getRetryCount());
                }
                return true;
            }

            @Override
            public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback,
                    Throwable throwable) {
                if (throwable == null) {
                    if (context.getRetryCount() > 0) {
                        log.info("Operation succeeded after {} retries", context.getRetryCount());
                    } else {
                        log.debug("Operation succeeded on first attempt");
                    }
                } else if (context.getRetryCount() > 0) {
                    log.error("Operation failed after {} retries. Last error: {}",
                            context.getRetryCount(), throwable.getMessage());
                }
            }
        };
    }

    /**
     * Creates a RetryTemplate that uses settings from application config
     * but adds our custom logging listener.
     * 
     * Note: The actual retry settings come from consul-config-dev.yml
     */
    @Bean
    @ConditionalOnMissingBean(RetryTemplate.class)
    public RetryTemplate retryTemplate(RetryListener loggingRetryListener) {
        RetryTemplate template = new RetryTemplate();
        template.registerListener(loggingRetryListener);
        return template;
    }
}