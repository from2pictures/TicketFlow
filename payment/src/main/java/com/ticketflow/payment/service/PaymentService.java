package com.ticketflow.payment.service;

import com.ticketflow.contracts.dto.request.PaymentRequest;
import com.ticketflow.contracts.dto.response.PaymentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
public class PaymentService {

    /** true → имитируем деградацию платёжного шлюза */
    private final AtomicBoolean failureMode = new AtomicBoolean(false);

    public void setFailureMode(boolean fail) {
        failureMode.set(fail);
        log.warn("⚠️ Payment failure mode = {}", fail);
    }

    public PaymentResponse charge(PaymentRequest request) {
        if (failureMode.get()) {
            // «Висим» 5 c — дольше feign read-timeout (2 c),
            // поэтому Booking засчитает вызов как ошибку и наполнит окно breaker'а.
            sleep(5000);
            throw new IllegalStateException("Платёжный шлюз не отвечает");
        }
        log.info("Платёж по брони {} на {} ₽ одобрен", request.bookingId(), request.amount());
        return new PaymentResponse(request.bookingId(), PaymentResponse.PaymentStatus.APPROVED, "Оплата прошла успешно");
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}