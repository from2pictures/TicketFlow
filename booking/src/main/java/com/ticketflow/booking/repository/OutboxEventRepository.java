package com.ticketflow.booking.repository;

import com.ticketflow.booking.entity.OutboxEvent;
import com.ticketflow.booking.status.EventStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, UUID> {
    List<OutboxEvent> findByStatusOrderByCreatedAtAsc(EventStatus status, Pageable pageable);
}
