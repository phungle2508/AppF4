package com.f4.reel.kafka.broker;

import java.util.function.Supplier;
import org.springframework.stereotype.Component;

import com.f4.reel.avro.EventEnvelope;
import com.f4.reel.kafka.service.KafkaUtilityService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Component("kafkaProducer")
public class KafkaProducer implements Supplier<EventEnvelope> {
    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);

    private final KafkaUtilityService kafkaUtilityService;
    private final AtomicReference<EventEnvelope> messageToSupply = new AtomicReference<>();

    @Value("${spring.cloud.stream.bindings.kafkaProducer-out-0.destination}")
    private String configuredOutputTopic;

    public KafkaProducer(KafkaUtilityService kafkaUtilityService) {
        this.kafkaUtilityService = kafkaUtilityService;
    }

    public boolean triggerReelEventPreparationAndDirectSend(UUID userId, String title, String videoUrl) {
        log.info("KafkaProducer: Delegating reel event preparation and direct send to KafkaUtilityService.");
        try {
            return kafkaUtilityService.producer_prepareAndAttemptDirectSendReelEvent(
                    userId, 
                    title, 
                    videoUrl, 
                    this.configuredOutputTopic, 
                    this.messageToSupply
            );
        } catch (Exception e) {
            log.error("KafkaProducer: Error while delegating to KafkaUtilityService for reel event: {}", e.getMessage(), e);
            this.messageToSupply.set(null);
            return false;
        }
    }

    @Override
    public EventEnvelope get() {
        EventEnvelope event = this.messageToSupply.getAndSet(null);
        if (event != null) {
            log.info("Supplier get() is supplying prepared message for event: {} to 'kafkaProducer-out-0' binding", event.getEventName());
        } else {
            log.trace("Supplier get() is returning null as no event was prepared or already taken.");
        }
        return event;
    }

}
