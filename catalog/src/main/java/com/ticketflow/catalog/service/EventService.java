package com.ticketflow.catalog.service;

import com.ticketflow.catalog.entity.Event;
import com.ticketflow.catalog.entity.Venue;
import com.ticketflow.catalog.exception.EventNotFoundException;
import com.ticketflow.catalog.exception.VenueNotFoundException;
import com.ticketflow.catalog.mapper.EventMapper;
import com.ticketflow.catalog.repository.EventRepository;
import com.ticketflow.catalog.repository.VenueRepository;
import com.ticketflow.contracts.dto.request.CreateEventRequest;
import com.ticketflow.contracts.dto.request.UpdateEventRequest;
import com.ticketflow.contracts.dto.response.EventResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {
    private final EventRepository eventRepo;
    private final EventMapper eventMapper;
    private final VenueRepository venueRepo;

    @Transactional(readOnly = true)
    public Page<EventResponse> getAll(Pageable pageable) {
        log.info("Fetching all events with pagination: {}", pageable);
        return eventRepo.findAll(pageable).map(eventMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "events", key = "#id")
    public EventResponse getById(UUID id) {
        log.info("Fetching event with id: {}", id);
        Event event = eventRepo.findById(id)
                .orElseThrow(() -> new EventNotFoundException(id));
        return eventMapper.toResponse(event);

    }

    @Transactional
    @CacheEvict(value = "events", allEntries = true)
    public EventResponse create(@Valid CreateEventRequest request) {
        log.info("Creating new event: {}", request.title());

        Venue venue = venueRepo.findById(request.venueId())
                .orElseThrow(() -> new VenueNotFoundException(request.venueId()));

        Event event = eventMapper.toEntity(request);
        event.setVenue(venue);

        eventRepo.save(event);
        return eventMapper.toResponse(event);
    }

    @Transactional
    @CacheEvict(value = "events", key = "#id")
    public EventResponse update(UUID id, UpdateEventRequest request) {
        log.info("Updating event with id: {}", id);
        Event event = eventRepo.findById(id)
                .orElseThrow(() -> new EventNotFoundException(id));
        eventMapper.updateEntity(request, event);
        Event updatedEvent = eventRepo.save(event);
        return eventMapper.toResponse(updatedEvent);
    }

    @Transactional
    @CacheEvict(value = "events", key = "#id")
    public void delete(UUID id) {
        log.info("Deleting event with id: {}", id);
        if (!eventRepo.existsById(id)) {
            throw new EventNotFoundException(id);
        }
        eventRepo.deleteById(id);
    }
}
