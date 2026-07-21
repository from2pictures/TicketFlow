package com.ticketflow.contracts.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentRequest(
        UUID bookingId,
        UUID userId,
        BigDecimal amount
)
{}
