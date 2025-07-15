package com.kims.coreevent.listener;


import com.kims.coreevent.event.DomainEvent;

public interface DomainEventHandler<T extends DomainEvent> {
    void handle(T t);
    Class<T> getEventType();
    default boolean supports(Object event) {
        return event != null && getEventType().isAssignableFrom(event.getClass());
    }
}
