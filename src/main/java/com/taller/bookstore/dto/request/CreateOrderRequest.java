package com.taller.bookstore.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateOrderRequest(
        @NotEmpty(message = "Debe enviar al menos un item")
        List<@Valid CreateOrderItemRequest> items
) {
}
