package com.ticketflow.orderservice.controller;

import com.ticketflow.contracts.events.TicketPurchasedEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @PostMapping("/simulate-purchase")
    public TicketPurchasedEvent simulatePurchase() {
        return TicketPurchasedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .userId(101L)
                .concertId(55L)
                .price(new BigDecimal("150.00"))
                .build();
    }

    @RequestMapping("/hello")
    public String hello() {
        return "Hello World!";
    }
}