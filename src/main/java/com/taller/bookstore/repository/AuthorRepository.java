package com.taller.bookstore.repository;

import com.taller.bookstore.entity.Author;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    boolean existsByNameIgnoreCase(String name);

    @EntityGraph(attributePaths = "books")
    @Query("select a from Author a where a.id = :id")
    Optional<Author> findWithBooksById(@Param("id") Long id);
}
