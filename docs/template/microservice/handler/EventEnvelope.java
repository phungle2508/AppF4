package com.f4.reel.handler;

// wraps any payload with an eventName

public class EventEnvelope<T> {
    private String eventName;
    private T payload;

    public EventEnvelope() {
    }

    public EventEnvelope(String eventName, T payload) {
        this.eventName = eventName;
        this.payload = payload;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

}
