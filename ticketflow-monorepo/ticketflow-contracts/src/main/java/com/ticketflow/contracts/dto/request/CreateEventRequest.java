package com.ticketflow.contracts.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Schema(description = "Запрос на создание события")
public record CreateEventRequest(
    @NotBlank(message = "Название обязательно")
    @Size(max = 255)
    @Schema(description = "Название концерта", example = "Rock Festival 2026")
    String title,

    @Size(max = 5000)
    String description,

    @NotNull(message = "ID площадки обязателен")
    UUID venueId,

    @NotNull(message = "Время начала обязательно")
    @Future(message = "Время должно быть в будущем")
    Instant startTime,

    @Min(value = 1, message = "Длительность должна быть больше 0")
    Integer durationMinutes,

    @Min(0)
    Integer totalSeats,

    @NotNull
    @DecimalMin("0.01")
    BigDecimal basePrice
) {}