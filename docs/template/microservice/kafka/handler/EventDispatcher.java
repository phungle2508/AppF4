package com.f4.reel.kafka.handler;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Component
public class EventDispatcher {
    private static final Logger LOG = LoggerFactory.getLogger(EventDispatcher.class);
    
    private final Map<String, EventHandler<?>> handlers;
    ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public EventDispatcher(List<EventHandler<?>> list) {
        this.handlers = list.stream()
                .collect(Collectors.toMap(EventHandler::getEventName, h -> h));
    }

    /**
     * Dispatches events from JSON format
     * 
     * @param env The event envelope containing JsonNode payload
     * @throws Exception If dispatching fails
     */
    public void dispatch(EventEnvelope<JsonNode> env) throws Exception {
        String name = env.getEventName();
        LOG.debug("Dispatching event: {}", name);
        
        EventHandler<?> handler = handlers.get(name);
        if (handler == null)
            throw new IllegalArgumentException("No handler for event: " + name);

        // Figure out T at runtime
        JavaType type = mapper.getTypeFactory()
                .constructType(((ParameterizedType) handler.getClass()
                        .getGenericInterfaces()[0]).getActualTypeArguments()[0]);
        
        Object dto = mapper.treeToValue(env.getPayload(), type);
        LOG.debug("Converted payload to type: {}", type);

        // noinspection unchecked
        ((EventHandler<Object>) handler).handle(dto);
        LOG.debug("Event handled successfully");
    }
}
