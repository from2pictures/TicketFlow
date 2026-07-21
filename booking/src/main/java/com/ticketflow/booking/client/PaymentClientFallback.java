package com.ticketflow.booking.client;

import com.ticketflow.contracts.dto.request.PaymentRequest;
import com.ticketflow.contracts.dto.response.PaymentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentClientFallback implements PaymentClient {

    @Override
    public PaymentResponse charge(PaymentRequest request) {
        log.warn("Payment недоступен → fallback для брони {}", request.bookingId());
        return new PaymentResponse(
                request.bookingId(),
                PaymentResponse.PaymentStatus.UNAVAILABLE,
                "Сервис оплаты недоступен, попробуйте позже");
    }
}