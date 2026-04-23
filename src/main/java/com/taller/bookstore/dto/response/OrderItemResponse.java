package com.taller.bookstore.dto.response;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long id,
        Long bookId,
        String bookTitle,
        String isbn,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {
}
