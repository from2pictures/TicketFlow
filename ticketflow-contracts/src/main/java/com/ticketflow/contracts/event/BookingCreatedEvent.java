package com.ticketflow.contracts.event;

import java.math.BigDecimal;
import java.util.UUID;

public record BookingCreatedEvent(
        UUID bookingId,
        UUID concertId,
        UUID userId,
        Integer quantity,
        BigDecimal totalAmount)
{}
