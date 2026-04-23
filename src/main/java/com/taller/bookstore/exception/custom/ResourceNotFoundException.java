package com.taller.bookstore.exception.custom;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resource, String field, Object value) {
        super("%s with %s %s not found".formatted(resource, field, value));
    }
}
