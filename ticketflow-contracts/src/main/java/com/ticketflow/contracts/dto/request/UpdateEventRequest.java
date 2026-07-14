package com.ticketflow.contracts.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Schema(description = "Запрос на создание нового события (концерта)")
public record UpdateEventRequest(

        @Size(max = 255, message = "Название не может превышать 255 символов")
        @Schema(description = "Название концерта", example = "Rock Festival 2026")
        String title,

        @Size(max = 5000, message = "Описание не может превышать 5000 символов")
        @Schema(description = "Подробное описание события", example = "Лучшие хиты всех времен")
        String description,

        @Schema(description = "Ссылка на постер/изображение события", example = "https://example.com/poster.jpg")
        @Pattern(regexp = "^(https?://.*)?$", message = "Некорректный формат URL изображения")
        String imageUrl,

        @Pattern(
                regexp = "^(ROCK|POP|JAZZ|CLASSICAL|ELECTRONIC|HIP_HOP|THEATER|STAND_UP|OTHER)$",
                message = "Недопустимый жанр. Допустимые значения: ROCK, POP, JAZZ, CLASSICAL, ELECTRONIC, HIP_HOP, THEATER, STAND_UP, OTHER"
        )
        @Schema(description = "Жанр события", example = "ROCK")
        String genre,

        @Schema(description = "UUID площадки, где пройдет событие")
        UUID venueId,

        @Future(message = "Время начала должно быть в будущем")
        @Schema(description = "Дата и время начала события (UTC)", example = "2026-08-15T19:00:00Z")
        Instant startTime,

        @Min(value = 1, message = "Длительность должна быть не менее 1 минуты")
        @Schema(description = "Длительность события в минутах", example = "120")
        Integer durationMinutes,

        @Positive(message = "Количество мест должно быть больше нуля")
        @Schema(description = "Общая вместимость зала для этого события", example = "500")
        Integer totalSeats,

        @PositiveOrZero(message = "Базовая цена не может быть отрицательной")
        @Digits(integer = 8, fraction = 2, message = "Некорректный формат цены (макс. 8 цифр до запятой, 2 после)")
        @Schema(description = "Стоимость билета", example = "1499.99")
        BigDecimal basePrice

) {}