package com.taller.bookstore.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record BookResponse(
        Long id,
        String title,
        String description,
        BigDecimal price,
        Integer stock,
        String isbn,
        AuthorResponse author,
        List<CategoryResponse> categories
) {
}
