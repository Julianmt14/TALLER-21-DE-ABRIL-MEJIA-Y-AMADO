package com.taller.bookstore.exception.custom;

public class AuthorHasBooksException extends RuntimeException {

    public AuthorHasBooksException(Long authorId) {
        super("author %d cannot be deleted because it has associated books".formatted(authorId));
    }
}
