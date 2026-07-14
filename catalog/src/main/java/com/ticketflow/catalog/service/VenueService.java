package com.ticketflow.catalog.service;

import com.ticketflow.catalog.entity.Venue;
import com.ticketflow.catalog.exception.VenueNotFoundException;
import com.ticketflow.catalog.mapper.VenueMapper;
import com.ticketflow.catalog.repository.VenueRepository;
import com.ticketflow.contracts.dto.request.CreateVenueRequest;
import com.ticketflow.contracts.dto.request.UpdateVenueRequest;
import com.ticketflow.contracts.dto.response.VenueSmallResponse;
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
public class VenueService {
    private final VenueRepository repository;
    private final VenueMapper mapper;

    @Transactional(readOnly = true)
    @Cacheable(value = "venues", key = "#id", unless = "#result == null")
    public VenueSmallResponse getById(UUID id) {
        log.info("Fetching venue with id: {}", id);
        Venue venue = repository.findById(id)
                .orElseThrow(() -> new VenueNotFoundException(id));
        return mapper.toResponse(venue);
    }

    @Transactional(readOnly = true)
    public Page<VenueSmallResponse> getAll(Pageable pageable) {
        log.info("Fetching all venues with pagination: {}", pageable);
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Transactional
    @CacheEvict(value = "venues", allEntries = true)
    public VenueSmallResponse create(CreateVenueRequest request) {
        log.info("Creating new venue: {}", request.name());
        Venue venue = mapper.toEntity(request);
        venue.setStatus("ACTIVE");
        return mapper.toResponse(repository.save(venue));
    }

    @Transactional
    @CacheEvict(value = "venues", key = "#id")
    public VenueSmallResponse update(UUID id, UpdateVenueRequest request) {
        log.info("Updating venue with id: {}", id);
        Venue venue = repository.findById(id)
                .orElseThrow(() -> new VenueNotFoundException(id));
        mapper.updateEntity(request, venue);
        Venue updatedVenue = repository.save(venue);
        return mapper.toResponse(updatedVenue);
    }

    @Transactional
    @CacheEvict(value = "venues", key = "#id")
    public void delete(UUID id) {
        log.info("Soft-deleting venue with id: {}", id);
        if(!repository.existsById(id)) throw new VenueNotFoundException(id);
        repository.deleteById(id);
    }
}