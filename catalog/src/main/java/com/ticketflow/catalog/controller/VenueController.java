package com.ticketflow.catalog.controller;

import com.ticketflow.catalog.service.VenueService;
import com.ticketflow.contracts.dto.request.CreateVenueRequest;
import com.ticketflow.contracts.dto.request.UpdateVenueRequest;
import com.ticketflow.contracts.dto.response.VenueSmallResponse;
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
@RequestMapping("/api/v1/venues")
@RequiredArgsConstructor
@Tag(name = "Venues", description = "API для управления местами")
public class VenueController {

    private final VenueService service;

    @GetMapping
    @Operation(summary = "Получить все места с пагинацией")
    public ResponseEntity<Page<VenueSmallResponse>> getAll(
            @Parameter(description = "Параметры пагинации") Pageable pageable) {
        return ResponseEntity.ok(service.getAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить место по ID")
    @ApiResponse(responseCode = "200", description = "Место найдено")
    @ApiResponse(responseCode = "404", description = "Место не найдено")
    public ResponseEntity<VenueSmallResponse> getById(
            @PathVariable("id") @Parameter(description = "UUID места") UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    @Operation(summary = "Создать новое место")
    @ApiResponse(responseCode = "201", description = "Место создано")
    public ResponseEntity<VenueSmallResponse> create(
            @Valid @RequestBody CreateVenueRequest request) {
        VenueSmallResponse created = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновить поля места")
    @ApiResponse(responseCode = "200", description = "Место обновлено")
    @ApiResponse(responseCode = "404", description = "Место не найдено")
    public ResponseEntity<VenueSmallResponse> update(@PathVariable("id") UUID id,
                                                     @RequestBody UpdateVenueRequest request) {
        VenueSmallResponse updated = service.update(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить место")
    @ApiResponse(responseCode = "204", description = "Место удалено")
    @ApiResponse(responseCode = "404", description = "Место не найдено")
    public ResponseEntity<Void> delete(
            @Parameter(description = "UUID места") @PathVariable("id") UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}