package com.f4.feed.broker;

import static org.springframework.web.servlet.mvc.method.annotation.SseEmitter.event;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class KafkaConsumer implements Consumer<String> {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaConsumer.class);

    private Map<String, SseEmitter> emitters = new HashMap<>();

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
    public void accept(String input) {
        LOG.debug("Got message from kafka stream: {}", input);
        emitters
            .entrySet()
            .stream()
            .map(Map.Entry::getValue)
            .forEach((SseEmitter emitter) -> {
                try {
                    emitter.send(event().data(input, MediaType.TEXT_PLAIN));
                } catch (IOException e) {
                    LOG.debug("error sending sse message, {}", input);
                }
            });
    }
}
