package com.ticketflow.contracts.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketPurchasedEvent {
    private String eventId;
    private Long userId;
    private Long concertId;
    private BigDecimal price;
}