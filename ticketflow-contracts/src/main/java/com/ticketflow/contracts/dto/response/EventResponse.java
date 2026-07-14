package com.ticketflow.contracts.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record EventResponse(
        UUID id,
        String title,
        String description,
        String imageUrl,
        Instant startTime,
        Integer durationMinutes,
        Integer availableSeats,
        BigDecimal basePrice,
        String status,
        VenueSmallResponse venue
) {}