package com.ticketflow.booking.service;

import com.ticketflow.booking.client.PaymentClient;
import com.ticketflow.contracts.dto.request.PaymentRequest;
import com.ticketflow.contracts.dto.response.BookingResponse;
import com.ticketflow.contracts.dto.response.PaymentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentGatewayService {

    private final PaymentClient paymentClient;
    private final BookingService bookingService;

    public PaymentResponse charge(BookingResponse booking) {
        PaymentResponse response = paymentClient.charge(new PaymentRequest(
                booking.id(),
                booking.userId(),
                booking.totalPrice()));

        switch (response.status()) {
            case APPROVED -> bookingService.confirmBooking(booking.id());
            case DECLINED -> bookingService.markPaymentFailed(booking.id(), response.message());
            case UNAVAILABLE -> {
                bookingService.markPendingPayment(booking.id());
                log.warn("Деградация Payment: бронь {} → PENDING_PAYMENT", booking.id());
            }
        }
        return response;
    }
}