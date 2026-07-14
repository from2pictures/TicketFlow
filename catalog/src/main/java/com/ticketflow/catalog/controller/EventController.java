package com.ticketflow.catalog.controller;

import com.ticketflow.catalog.service.EventService;
import com.ticketflow.contracts.dto.request.CreateEventRequest;
import com.ticketflow.contracts.dto.request.UpdateEventRequest;
import com.ticketflow.contracts.dto.response.EventResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@Tag(name = "Events", description = "API для управления концертами")
public class EventController {

    private final EventService service;

    @GetMapping
    @Operation(summary = "Получить все концерты с пагинацией")
    public ResponseEntity<Page<EventResponse>> getAll(
            @Parameter(description = "Параметры пагинации") Pageable pageable) {
        return ResponseEntity.ok(service.getAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить концерт по ID")
    @ApiResponse(responseCode = "200", description = "Концерт найден")
    @ApiResponse(responseCode = "404", description = "Концерт не найден")
    public ResponseEntity<EventResponse> getById(
            @Parameter(description = "UUID концерта") @PathVariable("id") UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    @Operation(summary = "Создать новый концерт")
    @ApiResponse(responseCode = "201", description = "Концерт создан")
    public ResponseEntity<EventResponse> create(
            @Valid @RequestBody CreateEventRequest request) {
        EventResponse created = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновить поля мероприятия")
    @ApiResponse(responseCode = "200", description = "Мероприятие обновлено")
    @ApiResponse(responseCode = "404", description = "Мероприятие не найдено")
    public ResponseEntity<EventResponse> update(@PathVariable("id") UUID id,
                                                @RequestBody UpdateEventRequest request) {
        EventResponse updated = service.update(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить концерт")
    @ApiResponse(responseCode = "204", description = "Концерт удален")
    public ResponseEntity<Void> delete(
            @Parameter(description = "UUID концерта") @PathVariable("id") UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}