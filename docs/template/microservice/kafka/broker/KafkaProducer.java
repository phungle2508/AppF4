package com.f4.reel.kafka.broker;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.f4.reel.avro.EventEnvelope;
import com.f4.reel.kafka.service.KafkaUtilityService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.KafkaHeaders;

@Component("kafkaProducer")
public class KafkaProducer implements Supplier<Message<EventEnvelope>> {
    private final KafkaUtilityService kafkaUtilityService;
    private final AtomicReference<EventEnvelope> messageToSupply = new AtomicReference<>();
    private final AtomicReference<String> keyToSupply = new AtomicReference<>();
    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);
    @Value("${spring.cloud.stream.bindings.kafkaProducer-out-0.destination}")
    private String configuredOutputTopic;

    public KafkaProducer(KafkaUtilityService kafkaUtilityService) {
        this.kafkaUtilityService = kafkaUtilityService;
    }

    public String send(String eventName, UUID userId, String title, String videoUrl) {
        log.info("KafkaProducer: Preparing reel event and generating unique key.");
        return kafkaUtilityService.producer_prepareAndAttemptDirectSendReelEvent(
                eventName, userId, title, videoUrl,
                configuredOutputTopic, messageToSupply, keyToSupply);
    }

    @Override
    public Message<EventEnvelope> get() {
        EventEnvelope event = messageToSupply.getAndSet(null);
        String key = keyToSupply.getAndSet(null);
        if (event != null) {
            log.info("Supplying event with unique key: {} to kafkaProducer-out-0 binding", key);
            return MessageBuilder.withPayload(event)
                    .setHeader(KafkaHeaders.KEY, key)
                    .build();
        } else {
            log.trace("No event to supply, returning null.");
            return null;
        }
    }

}
