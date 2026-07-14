package com.ticketflow.contracts.dto.response;

import java.util.UUID;

public record VenueSmallResponse(
        UUID id,
        String name,
        String city,
        String address,
        String venueType,
        Integer capacity
)
{}
