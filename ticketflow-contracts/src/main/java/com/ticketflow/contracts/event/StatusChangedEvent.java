package com.ticketflow.contracts.event;

import java.util.UUID;

public record StatusChangedEvent(
        UUID bookingId,
        String detail
)
{}
