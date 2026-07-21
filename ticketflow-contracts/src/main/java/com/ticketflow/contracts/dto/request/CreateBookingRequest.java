package com.ticketflow.contracts.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateBookingRequest(

        @NotNull(message = "userId обязателен")
        UUID userId,

        @NotNull(message = "eventId обязателен")
        UUID eventId,

        @NotNull(message = "quantity обязателен")
        @Min(value = 1, message = "quantity должен быть не меньше 1")
        @Max(value = 20, message = "quantity не может быть больше 20")
        Integer quantity,

        @NotNull(message = "unitPrice обязателен")
        @DecimalMin(value = "0.01", message = "unitPrice должен быть больше 0")
        @Digits(
                integer = 8,
                fraction = 2,
                message = "unitPrice должен содержать максимум 8 цифр до запятой и 2 знака после запятой"
        )
        BigDecimal unitPrice
) {
}