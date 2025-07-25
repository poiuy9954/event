package com.kims.coreevent.repository;

import com.kims.coreevent.entity.EventHist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventHistRepository extends JpaRepository<EventHist, String> {

    public List<EventHist> findByEventCreateTimeBeforeAndEventStatus(LocalDateTime currntTime, EventHist.EventStatus eventStatus);
}
