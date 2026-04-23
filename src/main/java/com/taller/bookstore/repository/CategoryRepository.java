package com.taller.bookstore.repository;

import com.taller.bookstore.entity.Category;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByNameIgnoreCase(String name);

    @EntityGraph(attributePaths = "books")
    @Query("select c from Category c where c.id = :id")
    Optional<Category> findWithBooksById(@Param("id") Long id);
}
