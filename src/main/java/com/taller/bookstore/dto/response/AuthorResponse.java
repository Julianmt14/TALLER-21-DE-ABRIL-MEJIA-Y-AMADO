package com.taller.bookstore.dto.response;

public record AuthorResponse(
        Long id,
        String name,
        String biography,
        String contactInfo
) {
}
