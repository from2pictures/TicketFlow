package com.ticketflow.catalog.service;

import com.ticketflow.catalog.entity.Event;
import com.ticketflow.catalog.mapper.EventMapper;
import com.ticketflow.catalog.repository.EventRepository;
import com.ticketflow.contracts.dto.response.EventResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {
    private final EventRepository repository;
    private final EventMapper mapper;

    @Transactional(readOnly = true)
    public Page<EventResponse> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    public ResponseEntity<?> getById(UUID id) {
        Optional<Event> event = repository.findById(id);
        if (event.isPresent()) {
            return ResponseEntity.ok(event.get());
        }
        return ResponseEntity.notFound().build();

    }

    public ResponseEntity<Event> add(Event event) {
        return ResponseEntity.ok(repository.save(event));
    }

    public ResponseEntity<Void> deleteById(UUID id) {
        Optional<Event> event = repository.findById(id);
        if(event.isPresent()) {
            repository.delete(event.get());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
