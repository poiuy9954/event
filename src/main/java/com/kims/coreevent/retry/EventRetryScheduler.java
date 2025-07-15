package com.kims.coreevent.retry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kims.coreevent.entity.EventHist;
import com.kims.coreevent.event.DomainEvent;
import com.kims.coreevent.publisher.DomainEventPublisher;
import com.kims.coreevent.service.EventHistService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class EventRetryScheduler {

    private final EventHistService eventHistService;
    private final DomainEventPublisher publisher;
    private final ObjectMapper eom;

    public EventRetryScheduler(EventHistService eventHistService, DomainEventPublisher publisher, ObjectMapper eventObjectMapper) {
        this.eventHistService = eventHistService;
        this.publisher = publisher;
        this.eom = eventObjectMapper;
    }


    @Scheduled(fixedDelay = 600000)
    public void run(){

        //미처리 이벤트 조회(이벤트 발행시간 기준 10분 동안 pending 상태인 이벤트)
        List<EventHist> eventHists = eventHistService.findEventHistBy10MinBefore();
        if(!eventHists.isEmpty()){
            EventHist eventHist = eventHists.get(0);
            DomainEvent doe = null;
            try {
                doe = this.generateEvent(eventHist);
            } catch (ClassNotFoundException | JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            publisher.publish(doe);
        }
    }

    private DomainEvent generateEvent(EventHist eventHist) throws ClassNotFoundException, JsonProcessingException {
        Class<?> eventType = Class.forName(eventHist.getEventType());
        Object obj = eom.readValue(eventHist.getPayload(),eventType);
        return (DomainEvent) obj;
    }
}
