package com.taller.bookstore.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthorRequest(
        @NotBlank(message = "El nombre del autor es obligatorio")
        @Size(max = 120, message = "El nombre del autor no puede superar 120 caracteres")
        String name,
        @Size(max = 2000, message = "La biografia no puede superar 2000 caracteres")
        String biography,
        @Size(max = 255, message = "La informacion de contacto no puede superar 255 caracteres")
        String contactInfo
) {
}
