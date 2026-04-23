package com.taller.bookstore.dto.response;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        Long userId,
        String userEmail,
        String status,
        BigDecimal total,
        List<OrderItemResponse> items,
        OffsetDateTime createdAt
) {
}
