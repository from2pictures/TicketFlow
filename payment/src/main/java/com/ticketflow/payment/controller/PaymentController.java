package com.ticketflow.payment.controller;

import com.ticketflow.contracts.dto.request.PaymentRequest;
import com.ticketflow.contracts.dto.response.PaymentResponse;
import com.ticketflow.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /** Основной эндпоинт: вызывается из Booking Service через OpenFeign. */
    @PostMapping
    public PaymentResponse charge(@RequestBody PaymentRequest request) {
        return paymentService.charge(request);
    }

    /**
     * Рубильник для демонстрации Circuit Breaker у Booking:
     *   POST /api/payments/simulate?fail=true   → сервис «лежит» (5 c + ошибка)
     *   POST /api/payments/simulate?fail=false  → сервис снова здоров
     */
    @PostMapping("/simulate")
    public Map<String, Object> simulate(@RequestParam(defaultValue = "true") boolean fail) {
        paymentService.setFailureMode(fail);
        return Map.of(
                "failureMode", fail,
                "hint", fail ? "Теперь дёргайте POST /api/bookings/{id}/pay и смотрите на breaker"
                        : "Payment снова здоров — breaker восстановится через HALF_OPEN");
    }
}