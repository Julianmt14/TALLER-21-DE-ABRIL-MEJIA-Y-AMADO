package com.taller.bookstore.service;

import com.taller.bookstore.dto.request.AuthorRequest;
import com.taller.bookstore.dto.response.AuthorResponse;
import com.taller.bookstore.dto.response.BookResponse;

import java.util.List;

public interface AuthorService {

    List<AuthorResponse> getAuthors();

    AuthorResponse getAuthorById(Long id);

    AuthorResponse createAuthor(AuthorRequest request);

    AuthorResponse updateAuthor(Long id, AuthorRequest request);

    void deleteAuthor(Long id);

    List<BookResponse> getBooksByAuthor(Long id);
}
