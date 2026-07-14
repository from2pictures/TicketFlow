package com.ticketflow.contracts.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Schema(description = "Запрос на создание новой площадки (venue)")
public record CreateVenueRequest(

        @NotBlank(message = "Название площадки обязательно")
        @Size(max = 255, message = "Название не может превышать 255 символов")
        @Schema(description = "Название площадки", example = "Dubai Arena")
        String name,

        @Size(max = 5000, message = "Описание не может превышать 5000 символов")
        @Schema(description = "Подробное описание площадки", example = "Крупнейшая крытая арена на Ближнем Востоке")
        String description,

        @Size(max = 500, message = "URL логотипа слишком длинный")
        @Pattern(regexp = "^(https?://.*)?$", message = "Некорректный формат URL логотипа")
        @Schema(description = "Ссылка на логотип площадки", example = "https://example.com/logo.png")
        String logoUrl,

        @NotBlank(message = "Страна обязательна")
        @Size(max = 100)
        @Schema(description = "Страна", example = "UAE")
        String country,

        @NotBlank(message = "Город обязателен")
        @Size(max = 100)
        @Schema(description = "Город", example = "Dubai")
        String city,

        @NotBlank(message = "Адрес обязателен")
        @Size(max = 500)
        @Schema(description = "Полный адрес площадки", example = "Silicon Oasis, Sheikh Zayed Rd")
        String address,

        @Size(max = 20)
        @Pattern(regexp = "^[A-Z0-9\\-\\s]*$", message = "Некорректный формат почтового индекса")
        @Schema(description = "Почтовый индекс", example = "12345")
        String postalCode,

        @DecimalMin(value = "-90.0", message = "Широта должна быть от -90 до 90")
        @DecimalMax(value = "90.0", message = "Широта должна быть от -90 до 90")
        @Schema(description = "Географическая широта", example = "25.0657")
        BigDecimal latitude,

        @DecimalMin(value = "-180.0", message = "Долгота должна быть от -180 до 180")
        @DecimalMax(value = "180.0", message = "Долгота должна быть от -180 до 180")
        @Schema(description = "Географическая долгота", example = "55.1713")
        BigDecimal longitude,

        @NotBlank(message = "Тип площадки обязателен")
        @Pattern(
                regexp = "^(STADIUM|ARENA|THEATER|CLUB|HALL|OUTDOOR|OTHER)$",
                message = "Недопустимый тип. Допустимые: STADIUM, ARENA, THEATER, CLUB, HALL, OUTDOOR, OTHER"
        )
        @Schema(description = "Тип площадки", example = "ARENA")
        String venueType,

        @NotNull(message = "Вместимость обязательна")
        @Positive(message = "Вместимость должна быть больше нуля")
        @Max(value = 1000000, message = "Слишком большая вместимость (макс. 1 млн)")
        @Schema(description = "Максимальная вместимость площадки", example = "17000")
        Integer capacity,

        @Schema(description = "Есть ли нумерованные сидячие места (false = только танцпол/стоячие)", example = "true")
        Boolean hasSeating,

        @Size(max = 20)
        @Pattern(regexp = "^[+]?[0-9\\s\\-()]*$", message = "Некорректный формат телефона")
        @Schema(description = "Контактный телефон", example = "+971 4 123 4567")
        String phone,

        @Email(message = "Некорректный формат email")
        @Size(max = 255)
        @Schema(description = "Контактный email", example = "info@dubaiarena.ae")
        String email,

        @Size(max = 500)
        @Pattern(regexp = "^(https?://.*)?$", message = "Некорректный формат URL сайта")
        @Schema(description = "Официальный сайт", example = "https://dubaiarena.ae")
        String website

) {}