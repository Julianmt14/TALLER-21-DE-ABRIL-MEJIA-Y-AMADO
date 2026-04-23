package com.taller.bookstore.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
        @NotBlank(message = "El nombre de la categoria es obligatorio")
        @Size(max = 100, message = "El nombre de la categoria no puede superar 100 caracteres")
        String name,
        @Size(max = 1000, message = "La descripcion no puede superar 1000 caracteres")
        String description
) {
}
