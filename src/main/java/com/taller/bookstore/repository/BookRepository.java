package com.taller.bookstore.repository;

import com.taller.bookstore.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    boolean existsByIsbn(String isbn);

    boolean existsByAuthorId(Long authorId);

    @Query("""
            select distinct b from Book b
            left join fetch b.author
            left join fetch b.categories
            where b.id = :id
            """)
    Optional<Book> findDetailedById(@Param("id") Long id);

    @EntityGraph(attributePaths = {"author", "categories"})
    @Query("""
            select distinct b from Book b
            left join b.categories c
            where (:authorId is null or b.author.id = :authorId)
              and (:categoryId is null or c.id = :categoryId)
            """)
    Page<Book> findByFilters(@Param("authorId") Long authorId,
                             @Param("categoryId") Long categoryId,
                             Pageable pageable);

    @EntityGraph(attributePaths = {"author", "categories"})
    List<Book> findByAuthorId(Long authorId);

    @EntityGraph(attributePaths = {"author", "categories"})
    @Query("""
            select distinct b from Book b
            join b.categories c
            where c.id = :categoryId
            """)
    List<Book> findByCategoryId(@Param("categoryId") Long categoryId);
}
