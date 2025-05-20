package com.f4.reel.kafka.handler;

public interface EventHandler<T> {
    String getEventName(); // e.g. "postReel"

    void handle(T payload);
}
