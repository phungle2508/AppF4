package com.f4.reel.handler;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Component
public class EventDispatcher {
    private final Map<String, EventHandler<?>> handlers;
    ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public EventDispatcher(List<EventHandler<?>> list) {
        this.handlers = list.stream()
                .collect(Collectors.toMap(EventHandler::getEventName, h -> h));
    }

    public void dispatch(EventEnvelope<JsonNode> env) throws Exception {
        String name = env.getEventName();
        EventHandler<?> handler = handlers.get(name);
        if (handler == null)
            throw new IllegalArgumentException("No handler for " + name);

        // figure out T at runtime
        JavaType type = mapper.getTypeFactory()
                .constructType(((ParameterizedType) handler.getClass()
                        .getGenericInterfaces()[0]).getActualTypeArguments()[0]);
        Object dto = mapper.treeToValue(env.getPayload(), type);

        // noinspection unchecked
        ((EventHandler<Object>) handler).handle(dto);
    }
}
