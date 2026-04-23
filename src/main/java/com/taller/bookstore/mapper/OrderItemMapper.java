package com.taller.bookstore.mapper;

import com.taller.bookstore.dto.response.OrderItemResponse;
import com.taller.bookstore.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    @Mapping(target = "isbn", source = "book.isbn")
    @Mapping(target = "unitPrice", source = "book.price")
    OrderItemResponse toResponse(OrderItem orderItem);
}
