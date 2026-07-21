package com.ticketflow.booking.outbox;

import com.ticketflow.booking.entity.OutboxEvent;
import com.ticketflow.booking.repository.OutboxEventRepository;
import com.ticketflow.booking.status.EventStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class OutboxPublisher {

    private static final String TOPIC = "booking.events";
    private static final int BATCH_SIZE = 50;
    private static final int MAX_RETRIES = 5;

    private final OutboxEventRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Transactional
    @Scheduled(fixedDelayString = "${app.outbox.poll-interval-ms:2000}")
    public void publishPendingEvents() {
        List<OutboxEvent> pending = outboxRepository
                .findByStatusOrderByCreatedAtAsc(
                        EventStatus.PENDING,
                        PageRequest.of(0, BATCH_SIZE)
                );
        log.info("Outbox: найдено {} событий для публикации", pending.size());

        for (OutboxEvent event : pending) {
            try {
                kafkaTemplate.send(TOPIC, event.getAggregateId().toString(), event.getPayload());
                event.setPublishedAt(Instant.now());
                event.setStatus(EventStatus.PUBLISHED);
            }   catch (Exception e) {
                event.setRetryCount(event.getRetryCount() + 1);
                event.setLastError(e.getMessage());
                if(event.getRetryCount() >= MAX_RETRIES) {
                    event.setStatus(EventStatus.FAILED);
                    log.error("Событие {} не доставлено после {} попыток",
                            event.getId(), MAX_RETRIES, e);
                } else {
                    log.warn("Ошибка публикации события {}, попытка {}",
                            event.getId(), event.getRetryCount());
                }
            }
            outboxRepository.save(event);
        }
    }
}
