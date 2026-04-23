package com.taller.bookstore.repository;

import com.taller.bookstore.entity.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = {"user", "items", "items.book", "items.book.author", "items.book.categories"})
    List<Order> findAllByOrderByCreatedAtDesc();

    @EntityGraph(attributePaths = {"user", "items", "items.book", "items.book.author", "items.book.categories"})
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);

    @Query("""
            select distinct o from Order o
            left join fetch o.user
            left join fetch o.items i
            left join fetch i.book b
            left join fetch b.author
            left join fetch b.categories
            where o.id = :id
            """)
    Optional<Order> findDetailedById(@Param("id") Long id);
}
