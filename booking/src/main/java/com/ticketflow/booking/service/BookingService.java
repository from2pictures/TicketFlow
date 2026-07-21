package com.ticketflow.booking.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketflow.booking.entity.Booking;
import com.ticketflow.booking.entity.OutboxEvent;
import com.ticketflow.booking.exception.BookingNotFoundException;
import com.ticketflow.booking.mapper.BookingMapper;
import com.ticketflow.booking.repository.BookingRepository;
import com.ticketflow.booking.repository.OutboxEventRepository;
import com.ticketflow.booking.status.BookingStatus;
import com.ticketflow.booking.status.EventStatus;
import com.ticketflow.contracts.dto.request.CreateBookingRequest;
import com.ticketflow.contracts.event.BookingCreatedEvent;
import com.ticketflow.contracts.event.StatusChangedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final OutboxEventRepository outboxRepository;
    private final BookingMapper bookingMapper;
    private final ObjectMapper objectMapper;

    @Transactional
    public Booking createBooking(CreateBookingRequest request) {
        Booking booking = bookingMapper.toEntity(request);
        booking.setTotalPrice(booking.getUnitPrice().multiply(BigDecimal.valueOf(booking.getQuantity())));
        booking.setStatus(BookingStatus.PENDING);

        bookingRepository.save(booking);

        appendEvent(booking, "BookingCreated", new BookingCreatedEvent(
                booking.getId(),
                booking.getEventId(),
                booking.getUserId(),
                booking.getQuantity(),
                booking.getTotalPrice()));

        log.info("Бронь {} создана, событие в outbox (та же транзакция)", booking.getId());
        return booking;
    }

    @Transactional
    public void confirmBooking(UUID bookingId) {
        Booking booking = getOrThrow(bookingId);
        booking.setStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(booking);
        appendEvent(booking, "BookingConfirmed", new StatusChangedEvent(bookingId, "CONFIRMED"));
    }

    @Transactional
    public void markPaymentFailed(UUID bookingId, String reason) {
        Booking booking = getOrThrow(bookingId);
        booking.setStatus(BookingStatus.PAYMENT_FAILED);
        bookingRepository.save(booking);
        appendEvent(booking, "BookingPaymentFailed", new StatusChangedEvent(bookingId, reason));
    }

    @Transactional
    public void markPendingPayment(UUID bookingId) {
        Booking booking = getOrThrow(bookingId);
        booking.setStatus(BookingStatus.PENDING);
        bookingRepository.save(booking);
    }

    private Booking getOrThrow(UUID id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));
    }

    private void appendEvent(Booking booking, String eventType, Object payload) {
        OutboxEvent event = OutboxEvent.builder()
                .aggregateType("Booking")
                .aggregateId(booking.getId())
                .eventType(eventType)
                .payload(toJson(payload))
                .status(EventStatus.PENDING)
                .retryCount(0)
                .build();
        outboxRepository.save(event);
    }

    private String toJson(Object payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Не удалось сериализовать событие", e);
        }
    }
}