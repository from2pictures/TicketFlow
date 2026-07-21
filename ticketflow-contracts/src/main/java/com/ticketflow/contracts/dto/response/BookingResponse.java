package com.ticketflow.contracts.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record BookingResponse(
        UUID id,
        UUID userId,
        UUID eventId,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal totalPrice
    )
{}
