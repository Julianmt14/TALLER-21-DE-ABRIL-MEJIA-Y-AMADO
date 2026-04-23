package com.taller.bookstore.exception.custom;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(Long bookId, int requested, int available) {
        super("book %d has insufficient stock. requested=%d, available=%d"
                .formatted(bookId, requested, available));
    }
}
