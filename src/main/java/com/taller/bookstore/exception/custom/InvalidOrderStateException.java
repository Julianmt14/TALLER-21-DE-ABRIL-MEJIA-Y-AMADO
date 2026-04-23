package com.taller.bookstore.exception.custom;

public class InvalidOrderStateException extends RuntimeException {

    public InvalidOrderStateException(Long orderId, String message) {
        super("order %d %s".formatted(orderId, message));
    }
}
