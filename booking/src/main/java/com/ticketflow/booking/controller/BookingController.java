package com.ticketflow.booking.controller;

import com.ticketflow.booking.service.BookingService;
import com.ticketflow.booking.service.PaymentGatewayService;
import com.ticketflow.contracts.dto.request.CreateBookingRequest;
import com.ticketflow.contracts.dto.response.BookingResponse;
import com.ticketflow.contracts.dto.response.PaymentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final PaymentGatewayService paymentGateway;

    @PostMapping
    @Operation(summary = "Создать новую бронь")
    @ApiResponse(responseCode = "201", description = "Бронь создана")
    public ResponseEntity<BookingResponse> create(
            @Valid @RequestBody CreateBookingRequest request) {
        BookingResponse created = bookingService.createBooking(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить бронь по ID")
    @ApiResponse(responseCode = "200", description = "Бронь найдена")
    @ApiResponse(responseCode = "404", description = "Бронь не найдена")
    public ResponseEntity<BookingResponse> getById(
            @Parameter(description = "UUID брони") @PathVariable("id") UUID id) {
        return ResponseEntity.ok(bookingService.getById(id));
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<Map<String, Object>> pay(@PathVariable UUID id) {
        BookingResponse booking = bookingService.getById(id);

        PaymentResponse payment = paymentGateway.charge(booking);

        return ResponseEntity.ok(Map.of(
                "booking", booking,
                "payment", payment));
    }
}