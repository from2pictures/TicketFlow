package com.ticketflow.contracts.dto.response;

import java.time.Instant;
import java.util.Map;

public record ErrorResponse(
        int status,
        String message,
        Instant timestamp,
        Map<String, String> errors
) {
    public ErrorResponse(int status, String message, Instant timestamp) {
        this(status, message, timestamp, null);
    }
}
