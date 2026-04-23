package com.taller.bookstore.impl;

import com.taller.bookstore.dto.request.BookRequest;
import com.taller.bookstore.dto.response.BookResponse;
import com.taller.bookstore.entity.Author;
import com.taller.bookstore.entity.Book;
import com.taller.bookstore.entity.Category;
import com.taller.bookstore.exception.custom.DuplicateResourceException;
import com.taller.bookstore.exception.custom.ResourceNotFoundException;
import com.taller.bookstore.mapper.BookMapper;
import com.taller.bookstore.repository.AuthorRepository;
import com.taller.bookstore.repository.BookRepository;
import com.taller.bookstore.repository.CategoryRepository;
import com.taller.bookstore.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.Set;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final BookMapper bookMapper;

    public BookServiceImpl(BookRepository bookRepository,
                           AuthorRepository authorRepository,
                           CategoryRepository categoryRepository,
                           BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookResponse> getBooks(Long authorId, Long categoryId, Pageable pageable) {
        return bookRepository.findByFilters(authorId, categoryId, pageable).map(bookMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponse getBookById(Long id) {
        return bookMapper.toResponse(findDetailedBook(id));
    }

    @Override
    public BookResponse createBook(BookRequest request) {
        if (bookRepository.existsByIsbn(request.isbn())) {
            throw new DuplicateResourceException("book", "isbn", request.isbn());
        }

        Book book = bookMapper.toEntity(request);
        applyRelationships(book, request);
        return bookMapper.toResponse(bookRepository.save(book));
    }

    @Override
    public BookResponse updateBook(Long id, BookRequest request) {
        Book book = findDetailedBook(id);

        if (!book.getIsbn().equals(request.isbn()) && bookRepository.existsByIsbn(request.isbn())) {
            throw new DuplicateResourceException("book", "isbn", request.isbn());
        }

        bookMapper.updateEntity(book, request);
        applyRelationships(book, request);
        return bookMapper.toResponse(bookRepository.save(book));
    }

    @Override
    public void deleteBook(Long id) {
        Book book = findDetailedBook(id);
        bookRepository.delete(book);
    }

    private Book findDetailedBook(Long id) {
        return bookRepository.findDetailedById(id)
                .orElseThrow(() -> new ResourceNotFoundException("book", "id", id));
    }

    private void applyRelationships(Book book, BookRequest request) {
        Author author = authorRepository.findById(request.authorId())
                .orElseThrow(() -> new ResourceNotFoundException("author", "id", request.authorId()));

        Set<Category> categories = new LinkedHashSet<>(request.categoryIds().stream()
                .map(categoryId -> categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new ResourceNotFoundException("category", "id", categoryId)))
                .toList());

        book.setAuthor(author);
        book.setCategories(categories);
    }
}
