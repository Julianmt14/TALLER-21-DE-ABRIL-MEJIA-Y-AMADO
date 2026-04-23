package com.taller.bookstore.impl;

import com.taller.bookstore.dto.request.AuthorRequest;
import com.taller.bookstore.dto.response.AuthorResponse;
import com.taller.bookstore.dto.response.BookResponse;
import com.taller.bookstore.entity.Author;
import com.taller.bookstore.exception.custom.AuthorHasBooksException;
import com.taller.bookstore.exception.custom.DuplicateResourceException;
import com.taller.bookstore.exception.custom.ResourceNotFoundException;
import com.taller.bookstore.mapper.AuthorMapper;
import com.taller.bookstore.mapper.BookMapper;
import com.taller.bookstore.repository.AuthorRepository;
import com.taller.bookstore.repository.BookRepository;
import com.taller.bookstore.service.AuthorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final AuthorMapper authorMapper;
    private final BookMapper bookMapper;

    public AuthorServiceImpl(AuthorRepository authorRepository,
                             BookRepository bookRepository,
                             AuthorMapper authorMapper,
                             BookMapper bookMapper) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.authorMapper = authorMapper;
        this.bookMapper = bookMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthorResponse> getAuthors() {
        return authorRepository.findAll().stream().map(authorMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorResponse getAuthorById(Long id) {
        return authorMapper.toResponse(findAuthor(id));
    }

    @Override
    public AuthorResponse createAuthor(AuthorRequest request) {
        if (authorRepository.existsByNameIgnoreCase(request.name())) {
            throw new DuplicateResourceException("author", "name", request.name());
        }
        return authorMapper.toResponse(authorRepository.save(authorMapper.toEntity(request)));
    }

    @Override
    public AuthorResponse updateAuthor(Long id, AuthorRequest request) {
        Author author = findAuthor(id);
        if (!author.getName().equalsIgnoreCase(request.name()) && authorRepository.existsByNameIgnoreCase(request.name())) {
            throw new DuplicateResourceException("author", "name", request.name());
        }
        authorMapper.updateEntity(author, request);
        return authorMapper.toResponse(authorRepository.save(author));
    }

    @Override
    public void deleteAuthor(Long id) {
        Author author = authorRepository.findWithBooksById(id)
                .orElseThrow(() -> new ResourceNotFoundException("author", "id", id));
        if (!author.getBooks().isEmpty()) {
            throw new AuthorHasBooksException(id);
        }
        authorRepository.delete(author);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> getBooksByAuthor(Long id) {
        findAuthor(id);
        return bookRepository.findByAuthorId(id).stream().map(bookMapper::toResponse).toList();
    }

    private Author findAuthor(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("author", "id", id));
    }
}
