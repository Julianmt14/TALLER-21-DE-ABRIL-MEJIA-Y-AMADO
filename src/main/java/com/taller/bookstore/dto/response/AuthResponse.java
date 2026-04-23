package com.taller.bookstore.dto.response;

public record AuthResponse(
        String token,
        long expiresIn,
        String role
) {
}
