package com.ticketflow.contracts.dto.response;

import java.util.UUID;

public record PaymentResponse(
        UUID bookingId,
        PaymentStatus status,
        String message
) {
    public enum PaymentStatus { APPROVED, DECLINED, UNAVAILABLE }
}