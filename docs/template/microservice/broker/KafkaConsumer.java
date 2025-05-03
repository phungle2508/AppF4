package com.f4.reel.broker;

import static org.springframework.web.servlet.mvc.method.annotation.SseEmitter.event;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.f4.reel.handler.EventDispatcher;
import com.f4.reel.handler.EventEnvelope;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class KafkaConsumer implements Consumer<String> {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaConsumer.class);

    private final EventDispatcher dispatcher;
    private final ObjectMapper mapper = new ObjectMapper();
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public KafkaConsumer(EventDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public SseEmitter register(String key) {
        LOG.debug("Registering sse client for {}", key);
        SseEmitter emitter = new SseEmitter();
        emitter.onCompletion(() -> emitters.remove(key));
        emitters.put(key, emitter);
        return emitter;
    }

    public void unregister(String key) {
        LOG.debug("Unregistering sse emitter for: {}", key);
        Optional.ofNullable(emitters.get(key)).ifPresent(SseEmitter::complete);
    }

    @Override
    public void accept(String rawJson) {
        LOG.debug("Got message from kafka stream: {}", rawJson);
        try {
            // 1) Parse the JSON into our envelope
            EventEnvelope<JsonNode> env = mapper.readValue(
                    rawJson,
                    new TypeReference<EventEnvelope<JsonNode>>() {
                    });

            // 2) Dispatch to the right handler based on env.eventName
            dispatcher.dispatch(env);

            // 3) Broadcast the same envelope out over any open SSE connections
            emitters.values().forEach(emitter -> {
                try {
                    emitter.send(
                            SseEmitter.event()
                                    .name(env.getEventName()) // you can send the event name too
                                    .data(env.getPayload().toString(), MediaType.APPLICATION_JSON));
                } catch (IOException | IllegalStateException ex) {
                    LOG.debug("Failed to send to SSE client, removing emitter", ex);
                    emitters.values().remove(emitter);
                }
            });
        } catch (IOException e) {
            LOG.error("Error parsing EventEnvelope or broadcasting SSE", e);
            // let Spring retry or send to DLQ if you’ve enabled it
            throw new RuntimeException("Failed to handle incoming event", e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
