package com.ontwo.coreevent.retry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ontwo.coreevent.entity.EventHist;
import com.ontwo.coreevent.event.DomainEvent;
import com.ontwo.coreevent.publisher.DomainEventPublisher;
import com.ontwo.coreevent.service.EventHistService;
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
    public void run() throws ClassNotFoundException, JsonProcessingException {
        /**
         * 이벤트 재시도 스케줄러
         * 이벤트 재시도 로직을 구현합니다.
         * 이벤트 재시도는 실패한 이벤트를 다시 처리하는 기능입니다.
         *
         */

        //미처리 이벤트 조회(이벤트 발행시간 기준 10분 동안 pending 상태인 이벤트)
        List<EventHist> eventHists = eventHistService.findEventHistBy10MinBefore();
        if(!eventHists.isEmpty()){
            EventHist eventHist = eventHists.get(0);
            DomainEvent doe = this.generateEvent(eventHist);
            publisher.publish(doe);
        }


        //조회된 이벤트를 재처리

    }

    private DomainEvent generateEvent(EventHist eventHist) throws ClassNotFoundException, JsonProcessingException {
        Class<?> eventType = Class.forName(eventHist.getEventType());
        Object obj = eom.readValue(eventHist.getPayload(),eventType);
        return (DomainEvent) obj;
    }
}
