package com.taller.bookstore.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
        String fullName,
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email debe tener un formato valido")
        String email,
        @NotBlank(message = "La password es obligatoria")
        @Size(min = 8, message = "La password debe tener minimo 8 caracteres")
        String password
) {
}
