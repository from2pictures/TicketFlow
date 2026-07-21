package com.ticketflow.booking.client;

import com.ticketflow.contracts.dto.request.PaymentRequest;
import com.ticketflow.contracts.dto.response.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "paymentService",
        url = "${app.payment-service.url}",
        fallback = PaymentClientFallback.class
)
public interface PaymentClient {

    @PostMapping("/api/v1/payments")
    PaymentResponse charge(@RequestBody PaymentRequest request);
}
