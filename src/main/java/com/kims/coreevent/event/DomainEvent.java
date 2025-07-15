package com.kims.coreevent.event;

import java.time.LocalDateTime;
import java.util.UUID;


public abstract class DomainEvent {
    protected String eventId;
    protected LocalDateTime occurredOn;
    protected DomainEvent() {
        super();
        eventId= UUID.randomUUID().toString();
        occurredOn = LocalDateTime.now();
    }

    public String getEventId() {
        return eventId;
    }

    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }

}
