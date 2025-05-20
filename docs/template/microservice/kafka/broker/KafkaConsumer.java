package com.f4.reel.kafka.broker;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.f4.reel.avro.EventEnvelope;
import com.f4.reel.kafka.handler.EventDispatcher;
import com.f4.reel.kafka.service.KafkaUtilityService;
import com.f4.reel.service.dto.ReelDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.retry.support.RetryTemplate;

@Component
public class KafkaConsumer implements Consumer<EventEnvelope> {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaConsumer.class);

    private final EventDispatcher dispatcher;
    private final ObjectMapper mapper = new ObjectMapper();
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final RetryTemplate retryTemplate;
    private final KafkaUtilityService kafkaUtilityService;

    @Value("${ssh.service-name}-input.dlq")
    private String dlqTopic;

    @Value("${kafka.dlq.enabled:true}")
    private boolean dlqEnabled;

    public KafkaConsumer(EventDispatcher dispatcher, RetryTemplate retryTemplate,
            KafkaUtilityService kafkaUtilityService) {
        this.dispatcher = dispatcher;
        this.retryTemplate = retryTemplate;
        this.kafkaUtilityService = kafkaUtilityService;
        this.mapper.registerModule(new JavaTimeModule());
    }

    public SseEmitter register(String key) {
        LOG.debug("KafkaConsumer: Delegating SSE client registration for key {} to KafkaUtilityService.", key);
        return kafkaUtilityService.consumer_registerSseEmitter(key, this.emitters);
    }

    public void unregister(String key) {
        LOG.debug("KafkaConsumer: Delegating SSE client unregistration for key {} to KafkaUtilityService.", key);
        kafkaUtilityService.consumer_unregisterSseEmitter(key, this.emitters);
    }

    @Override
    public void accept(EventEnvelope avroMessage) {
        if (avroMessage == null) {
            LOG.error("Received null Avro message - possible deserialization issue");
            // Consider if a DLQ message should be sent even for a null avroMessage itself.
            // kafkaUtilityService.consumer_sendToDlq(null, new
            // NullPointerException("Received null Avro message"), this.dlqTopic,
            // this.dlqEnabled, "null");
            return;
        }

        LOG.debug("Got Avro message from kafka stream: {}", avroMessage);

        try {
            retryTemplate.execute(context -> {
                LOG.info("Processing message attempt {}", context.getRetryCount() + 1);
                ReelDTO serviceReelDTO = null; // For SSE dispatch and potentially other uses
                String eventName = null; // To ensure eventName is available for SSE even if payload processing fails

                try {
                    eventName = avroMessage.getEventName();
                    if (eventName == null) {
                        throw new IllegalArgumentException("Event name cannot be null");
                    }

                    if (avroMessage.getPayload() != null && "postReel".equals(eventName)) {
                        // Step 1: Map Avro DTO to Service DTO using utility service
                        serviceReelDTO = kafkaUtilityService.consumer_mapAvroToServiceReelDTO(avroMessage.getPayload());

                        if (serviceReelDTO != null) {
                            // Step 2: Prepare EventEnvelope for the main dispatcher using utility service
                            com.f4.reel.kafka.handler.EventEnvelope<JsonNode> envelopeForDispatcher = kafkaUtilityService
                                    .consumer_prepareEventEnvelopeForDispatcher(eventName, serviceReelDTO);

                            if (envelopeForDispatcher != null) {
                                dispatcher.dispatch(envelopeForDispatcher);
                            } else {
                                LOG.warn("Envelope for dispatcher was null after preparation for event: {}", eventName);
                                // Decide if this is an error state that should prevent SSE or throw exception
                            }
                        } else {
                            LOG.warn("Service ReelDTO was null after mapping for event: {}", eventName);
                            // Decide if this is an error state that should prevent SSE or throw exception
                        }
                    } else {
                        LOG.warn("Unsupported event type or missing payload: {}", eventName);
                    }

                    // Delegate SSE dispatch to KafkaUtilityService
                    // Always attempt to dispatch to SSE if eventName is present, even if
                    // serviceReelDTO is null (e.g. for error events or events without payload)
                    kafkaUtilityService.consumer_dispatchToSseClients(eventName, serviceReelDTO, this.emitters);

                    LOG.info("Successfully processed Avro message for event: {}", eventName);
                    return null;
                } catch (Exception e) {
                    LOG.error("Error processing Avro message (event: {}, attempt {}): {}",
                            (eventName != null ? eventName : "UNKNOWN_EVENT"),
                            context.getRetryCount() + 1,
                            e.getMessage(), e); // Log the exception too
                    throw e;
                }
            }, context -> {
                LOG.error("Message processing failed after {} attempts, sending to DLQ. Event: {}",
                        context.getRetryCount() + 1,
                        (avroMessage.getEventName() != null ? avroMessage.getEventName() : "UNKNOWN_EVENT"));
                Exception exception = (Exception) context.getLastThrowable();
                kafkaUtilityService.consumer_sendToDlq(avroMessage, exception, this.dlqTopic, this.dlqEnabled,
                        avroMessage.toString()); // avroMessage is not null here
                return null;
            });
        } catch (Exception e) {
            LOG.error(
                    "Fatal error in message processing that couldn't be recovered with retries: {}. Sending to DLQ. Event: {}",
                    e.getMessage(),
                    (avroMessage.getEventName() != null ? avroMessage.getEventName() : "UNKNOWN_EVENT"), e); // Log the
                                                                                                             // exception
            kafkaUtilityService.consumer_sendToDlq(avroMessage, e, this.dlqTopic, this.dlqEnabled,
                    avroMessage.toString()); // avroMessage is not null here
        }
    }
}
