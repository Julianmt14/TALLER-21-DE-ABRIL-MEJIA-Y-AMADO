package com.taller.bookstore.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.Set;

public record BookRequest(
        @NotBlank(message = "El titulo es obligatorio")
        @Size(max = 200, message = "El titulo no puede superar 200 caracteres")
        String title,
        @Size(max = 2000, message = "La descripcion no puede superar 2000 caracteres")
        String description,
        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.01", message = "El precio debe ser mayor que 0")
        BigDecimal price,
        @NotNull(message = "El stock es obligatorio")
        @Min(value = 0, message = "El stock debe ser mayor o igual a 0")
        @Max(value = 1000000, message = "El stock es demasiado alto")
        Integer stock,
        @NotBlank(message = "El ISBN es obligatorio")
        @Pattern(regexp = "^(97(8|9))?\\d{9}(\\d|X)$", message = "El ISBN no tiene un formato valido")
        String isbn,
        @NotNull(message = "El autor es obligatorio")
        Long authorId,
        @NotEmpty(message = "Debe enviar al menos una categoria")
        Set<Long> categoryIds
) {
}
