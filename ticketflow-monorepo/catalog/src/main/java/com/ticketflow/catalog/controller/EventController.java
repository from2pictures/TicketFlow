package com.ticketflow.catalog.controller;

import com.ticketflow.catalog.entity.Event;
import com.ticketflow.catalog.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor


public class EventController {

    private final EventService service;

    @GetMapping("/events")
    public ResponseEntity<List<Event>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/event/{uuid}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PostMapping("/events")
    public ResponseEntity<Event> add(@RequestBody Event event) {
        return service.add(event);
    }

    @DeleteMapping("/event/{uuid}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        return service.deleteById(id);
    }
}
