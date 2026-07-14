package com.ticketflow.contracts.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Запрос на частичное обновление площадки (PATCH)")
public record UpdateVenueRequest(

        @Size(max = 255, message = "Название не может превышать 255 символов")
        @Schema(description = "Новое название площадки", example = "Dubai Arena 2.0")
        String name,

        @Size(max = 5000, message = "Описание не может превышать 5000 символов")
        @Schema(description = "Новое описание", example = "Теперь с новым звуковым оборудованием")
        String description,

        @Size(max = 500, message = "URL логотипа слишком длинный")
        @Pattern(regexp = "^(https?://.*)?$", message = "Некорректный формат URL логотипа")
        @Schema(description = "Новая ссылка на логотип", example = "https://example.com/new-logo.png")
        String logoUrl,

        @Size(max = 500)
        @Schema(description = "Новый адрес", example = "New Silicon Oasis, Sheikh Zayed Rd")
        String address,

        @Size(max = 20)
        @Pattern(regexp = "^[+]?[0-9\\s\\-()]*$", message = "Некорректный формат телефона")
        @Schema(description = "Новый контактный телефон", example = "+971 4 999 9999")
        String phone,

        @Email(message = "Некорректный формат email")
        @Size(max = 255)
        @Schema(description = "Новый контактный email", example = "new-info@dubaiarena.ae")
        String email,

        @Size(max = 500)
        @Pattern(regexp = "^(https?://.*)?$", message = "Некорректный формат URL сайта")
        @Schema(description = "Новый официальный сайт", example = "https://new-dubaiarena.ae")
        String website

) {}