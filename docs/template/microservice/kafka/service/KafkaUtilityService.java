package com.f4.reel.kafka.service;

import com.f4.reel.avro.EventEnvelope;
import com.f4.reel.service.dto.ReelDTO;
import com.f4.reel.kafka.util.AvroConverter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.MediaType;
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

    private final StreamBridge streamBridge;
    private final ObjectMapper objectMapper;

    @Autowired
    public KafkaUtilityService(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    // --- Helper for KafkaProducer ---

    public boolean producer_prepareAndAttemptDirectSendReelEvent(
            UUID userId, String title, String videoUrl,
            String configuredOutputTopic,
            AtomicReference<EventEnvelope> messageToSupplyRef) {
        PRODUCER_LOG.info("Helper: Preparing Avro reel event. Target topic from config for 'kafkaProducer-out-0': {}",
                configuredOutputTopic);
        boolean sentDirectly = false;
        try {
            // Use AvroConverter to create the envelope
            EventEnvelope envelope = AvroConverter.createPostReelEvent(userId, title, videoUrl);

            messageToSupplyRef.set(envelope);
            PRODUCER_LOG.info("Helper: Reel event prepared and stored for supplier pattern via reference.");

            String directSendBinding = "kafkaProducer-out-0"; // This is the binding name
            PRODUCER_LOG.info("Helper: Attempting direct send to binding: '{}' with message for event: {}",
                    directSendBinding, envelope.getEventName());

            sentDirectly = streamBridge.send(directSendBinding, envelope);

            if (sentDirectly) {
                PRODUCER_LOG.info(
                        "Helper: SUCCESS: StreamBridge.send(\"{}\") returned true. The binder accepted the message.",
                        directSendBinding);
            } else {
                PRODUCER_LOG.warn(
                        "Helper: FAILURE: StreamBridge.send(\"{}\") returned false. Message was not accepted by the binder for sending.",
                        directSendBinding);
            }
            return sentDirectly;

        } catch (Exception e) {
            PRODUCER_LOG.error("Helper: Error during prepareAndAttemptDirectSendReelEvent: {}", e.getMessage(), e);
            messageToSupplyRef.set(null); // Clear if preparation or send fails
            throw new RuntimeException("Helper: Error in prepareAndAttemptDirectSendReelEvent: " + e.getMessage(), e);
        }
    }

    // --- Helpers for KafkaConsumer ---

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

    public void consumer_dispatchToSseClients(String eventName, ReelDTO reelDTO, Map<String, SseEmitter> emittersMap) {
        CONSUMER_LOG.debug("Helper: Dispatching event {} to {} SSE clients using Service DTO", eventName, emittersMap.size());
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
    public ReelDTO consumer_mapAvroToServiceReelDTO(com.f4.reel.avro.ReelDTO avroPayload) {
        CONSUMER_LOG.debug("KafkaUtilityService: Delegating Avro to Service DTO conversion to AvroConverter for payload: {}", avroPayload);
        try {
            return AvroConverter.convertToServiceReelDTO(avroPayload);
        } catch (Exception e) {
            CONSUMER_LOG.error("KafkaUtilityService: Error during Avro to Service DTO conversion delegated to AvroConverter: {}", e.getMessage(), e);
            // Rethrow or handle as per requirements, ensuring consistency with how consumer handles it
            throw e; // Propagate the exception (e.g., RuntimeException from AvroConverter)
        }
    }

    // New helper method to prepare the EventEnvelope for the old dispatcher
    public com.f4.reel.kafka.handler.EventEnvelope<JsonNode> consumer_prepareEventEnvelopeForDispatcher(
            String eventName, ReelDTO serviceDto) {
        if (serviceDto == null) {
            CONSUMER_LOG.warn("Helper: ServiceDTO is null, cannot prepare EventEnvelope for dispatcher for event: {}",
                    eventName);
            // Depending on requirements, might return an envelope with null payload or
            // throw an error
            // For now, let's return an envelope that might signify no data or an issue.
            // Or, the calling method should handle the null DTO before calling this.
            return new com.f4.reel.kafka.handler.EventEnvelope<>(eventName, null);
        }
        try {
            String reelJson = objectMapper.writeValueAsString(serviceDto); // Uses objectMapper from KafkaUtilityService
            JsonNode reelJsonNode = objectMapper.readTree(reelJson);
            return new com.f4.reel.kafka.handler.EventEnvelope<>(eventName, reelJsonNode);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            CONSUMER_LOG.error("Helper: Error serializing/deserializing service DTO for dispatcher: {}", e.getMessage(),
                    e);
            throw new RuntimeException("Helper: Failed to prepare EventEnvelope for dispatcher", e);
        }
    }
}