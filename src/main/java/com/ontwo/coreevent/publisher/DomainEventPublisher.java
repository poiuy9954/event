package com.ontwo.coreevent.publisher;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ontwo.coreevent.entity.EventHist;
import com.ontwo.coreevent.event.DomainEvent;
import com.ontwo.coreevent.service.EventHistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

import static com.ontwo.coreevent.entity.EventHist.EventStatus;

@Transactional("eventTransactionManager")
@Component
public class DomainEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(DomainEventPublisher.class);

    private final ApplicationEventPublisher publisher;
    private final EventHistService eventHistService;
    private final ObjectMapper eventObjectMapper;

    public DomainEventPublisher(ApplicationEventPublisher publisher, EventHistService eventHistRepository, ObjectMapper eventObjectMapper) {
        this.publisher = publisher;
        this.eventHistService = eventHistRepository;
        this.eventObjectMapper = eventObjectMapper;
    }


    public void publish(@NonNull DomainEvent event) {
        Assert.notNull(event, "Event must not be null");
        log.debug("Publishing event: {} with ID: {}", event.getClass().getName(), event.getEventId());
        EventHist eventHist;

        try {
            eventHist = EventHist.builder()
                .id(event.getEventId())
                .eventCreateTime(event.getOccurredOn())
                .eventType(event.getClass().getName())
                .payload(eventObjectMapper.writeValueAsString(event))
                .eventStatus(EventStatus.PENDING)
                .processYn(false)
                .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        //EventHist 저장
        eventHistService.saveEventHistTxRN(eventHist);

        //TransactionSynchronizationManager를 사용하여 트랜잭션 커밋 후 이벤트 발행
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        publisher.publishEvent(event);
                    }
                }
        );
    }
}
