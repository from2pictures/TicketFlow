package com.ticketflow.contracts.dto.response;

import java.math.BigDecimal;

public record VenueFullResponse(
    String name,
    String description,
    String logoUrl,
    String country,
    String city,
    String postalCode,
    BigDecimal latitude,
    BigDecimal longitude,
    String venueType,
    Integer capacity,
    Boolean hasSeating,
    String phone,
    String email,
    String website,
    String status
)
{}