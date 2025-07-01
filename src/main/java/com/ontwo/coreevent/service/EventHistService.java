package com.ontwo.coreevent.service;

import com.ontwo.coreevent.entity.EventHist;
import com.ontwo.coreevent.repository.EventHistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventHistService {

    private static final Logger log = LoggerFactory.getLogger(EventHistService.class);

    private final EventHistRepository eventHistRepository;

    public EventHistService(EventHistRepository eventHistRepository) {
        this.eventHistRepository = eventHistRepository;
    }

    public void saveEventHist(EventHist eventHist) {
        eventHistRepository.save(eventHist);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveEventHistTxRN(EventHist eventHist) {
        eventHistRepository.save(eventHist);
    }

    public EventHist findEventHistById(String eventId) {
        EventHist eventHist = eventHistRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("EventHist not found for ID: " + eventId));
        log.debug("Found EventHist: {}", eventHist);
        return eventHist;
    }

    public List<EventHist> findEventHistBy10MinBefore() {
        LocalDateTime currentTime = java.time.LocalDateTime.now().minusMinutes(10);
        return eventHistRepository.findByEventCreateTimeBeforeAndEventStatus(currentTime, EventHist.EventStatus.PENDING);
    }
}
