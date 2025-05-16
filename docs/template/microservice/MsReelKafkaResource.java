package com.f4.reel.web.rest;

import com.f4.reel.kafka.broker.KafkaConsumer;
import com.f4.reel.kafka.handler.EventEnvelope;
import com.fasterxml.jackson.databind.JsonNode;

import java.security.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

@RestController
@RequestMapping("/api/ms-reel-kafka")
public class MsReelKafkaResource {

    private static final String PRODUCER_BINDING_NAME = "binding-out-0";

    private static final Logger LOG = LoggerFactory.getLogger(MsReelKafkaResource.class);
    private final KafkaConsumer kafkaConsumer;
    private final StreamBridge streamBridge;

    public MsReelKafkaResource(StreamBridge streamBridge, KafkaConsumer kafkaConsumer) {
        this.streamBridge = streamBridge;
        this.kafkaConsumer = kafkaConsumer;
    }

    @PostMapping("/publish")
    public ResponseEntity<Void> publish(
            @RequestParam("event") String eventName,
            @RequestBody JsonNode payload) {
        EventEnvelope<JsonNode> env = new EventEnvelope<>(eventName, payload);
        boolean sent = streamBridge.send(PRODUCER_BINDING_NAME, env);
        return sent
                ? ResponseEntity.accepted().build()
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("/register")
    public ResponseBodyEmitter register(Principal principal) {
        return kafkaConsumer.register(principal.getName());
    }

    @GetMapping("/unregister")
    public void unregister(Principal principal) {
        kafkaConsumer.unregister(principal.getName());
    }
}
