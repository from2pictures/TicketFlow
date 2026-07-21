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
import com.ticketflow.contracts.dto.response.BookingResponse;
import com.ticketflow.contracts.event.BookingCreatedEvent;
import com.ticketflow.contracts.event.StatusChangedEvent;
import jakarta.validation.Valid;
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
    public BookingResponse createBooking(@Valid CreateBookingRequest request) {
        log.info("Creating new booking for event: {}", request.eventId());

        Booking booking = bookingMapper.toEntity(request);

        // 2. Получаем актуальную цену из базы данных (Защита от подмены цены на фронтенде)
        // Event event = eventRepository.findById(request.eventId())
        //         .orElseThrow(() -> new EntityNotFoundException("Event not found"));
        // booking.setUnitPrice(event.getPrice());

        // 3. Безопасный расчет стоимости (уже проверено валидацией @Valid)
        BigDecimal quantityBD = BigDecimal.valueOf(booking.getQuantity());
        booking.setTotalPrice(booking.getUnitPrice().multiply(quantityBD));
        booking.setStatus(BookingStatus.PENDING);

        // 4. Сохраняем и О Б Я З А Т Е Л Ь Н О присваиваем результат переменной
        // Если используется JPA/Hibernate, save() возвращает управляемую сущность с заполненным ID
        booking = bookingRepository.save(booking);

        // 5. Теперь booking.getId() гарантированно НЕ null
        appendEvent(booking, "BookingCreated", new BookingCreatedEvent(
                booking.getId(),
                booking.getEventId(),
                booking.getUserId(),
                booking.getQuantity(),
                booking.getTotalPrice()
        ));

        log.info("Бронь {} создана, событие в outbox (та же транзакция)", booking.getId());
        return bookingMapper.toResponse(booking);
    }

    @Transactional
    public void confirmBooking(UUID bookingId) {
        Booking booking = getOrThrow(bookingId);
        booking.setStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(booking);
        appendEvent(booking, "BookingConfirmed", new StatusChangedEvent(bookingId, "CONFIRMED"));
        log.info("Booking confirmed: {}", bookingId);
    }

    @Transactional
    public void markPaymentFailed(UUID bookingId, String reason) {
        Booking booking = getOrThrow(bookingId);
        booking.setStatus(BookingStatus.PAYMENT_FAILED);
        bookingRepository.save(booking);
        appendEvent(booking, "BookingPaymentFailed", new StatusChangedEvent(bookingId, reason));
        log.info("Booking failed {}, because: {}", bookingId, reason);
    }

    @Transactional
    public void markPendingPayment(UUID bookingId) {
        Booking booking = getOrThrow(bookingId);
        booking.setStatus(BookingStatus.PENDING);
        bookingRepository.save(booking);
        log.info("Booking pended: {}", bookingId);
    }

    @Transactional(readOnly = true)
    protected Booking getOrThrow(UUID id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));
    }

    @Transactional
    protected void appendEvent(Booking booking, String eventType, Object payload) {
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

    @Transactional(readOnly = true)
    public BookingResponse getById(UUID id) {
        log.info("Fetching booking with id: {}", id);
        Booking find = getOrThrow(id);
        return bookingMapper.toResponse(find);
    }
}