package com.kims.coreevent.listener;

import com.kims.coreevent.entity.EventHist;
import com.kims.coreevent.event.DomainEvent;
import com.kims.coreevent.service.EventHistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.kims.coreevent.entity.EventHist.EventStatus;

@Transactional("eventTransactionManager")
@Component
public class DomainEventListener {

    private final Logger log = LoggerFactory.getLogger(DomainEventListener.class);

    private final List<DomainEventHandler<? extends DomainEvent>> domainEventHandlers;
    private final EventHistService eventHistService;

    public DomainEventListener(List<DomainEventHandler<? extends DomainEvent>> domainEventHandlers, EventHistService eventHistRepository) {
        this.domainEventHandlers = domainEventHandlers;
        this.eventHistService = eventHistRepository;

        log.debug("DomainEventListener initialized with {} handlers size", domainEventHandlers.size());
    }


    @EventListener
    @Async("eventListenerExecutor")
    @SuppressWarnings("unchecked") //DomainEvent 형변환에서 발생하는 부분 제거
    public void onEvent(DomainEvent event) {
        log.debug("Received event: {} with ID: {}", event.getClass().getName(), event.getEventId());
        domainEventHandlers.stream()
            .filter(handler->handler.supports(event))
            .forEach(handler -> {
                EventHist eventHist = eventHistService.findEventHistById(event.getEventId());
                try {
                    this.preEventHistSave(eventHist);
                    ((DomainEventHandler<DomainEvent>) handler).handle(event);
                }catch (Exception e){
                    log.error("Error handling event: {} with ID: {}", event.getClass().getName(), event.getEventId(), e);
                    this.errorEventHistSave(eventHist,e.getMessage());
                }
                this.postEventHistSave(eventHist);
            });
    }
    private void preEventHistSave(EventHist param) {
        EventHist eventHist = param.toBuilder()
            .eventStatus(EventStatus.PROCESSING)
            .processYn(false)
            .build();
        eventHistService.saveEventHistTxRN(eventHist);
    }

    private void postEventHistSave(EventHist param) {
        EventHist eventHist = param.toBuilder()
                .eventStatus(EventStatus.COMPLETED)
                .processYn(true)
                .resultMsg("Completed Subscription")
                .build();
        eventHistService.saveEventHistTxRN(eventHist);
    }

    private void errorEventHistSave(EventHist param, String errorMsg) {
        EventHist eventHist = param.toBuilder()
                .eventStatus(EventStatus.FAILED)
                .processYn(false)
                .resultMsg("Failed to process event: "+errorMsg)
                .build();
        eventHistService.saveEventHistTxRN(eventHist);
    }

}
