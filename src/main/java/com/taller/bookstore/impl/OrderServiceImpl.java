package com.taller.bookstore.impl;

import com.taller.bookstore.dto.request.CreateOrderItemRequest;
import com.taller.bookstore.dto.request.CreateOrderRequest;
import com.taller.bookstore.dto.response.OrderResponse;
import com.taller.bookstore.entity.Book;
import com.taller.bookstore.entity.Order;
import com.taller.bookstore.entity.OrderItem;
import com.taller.bookstore.entity.OrderStatus;
import com.taller.bookstore.entity.User;
import com.taller.bookstore.exception.custom.InsufficientStockException;
import com.taller.bookstore.exception.custom.InvalidOrderStateException;
import com.taller.bookstore.exception.custom.ResourceNotFoundException;
import com.taller.bookstore.exception.custom.UnauthorizedAccessException;
import com.taller.bookstore.mapper.OrderMapper;
import com.taller.bookstore.repository.BookRepository;
import com.taller.bookstore.repository.OrderRepository;
import com.taller.bookstore.repository.UserRepository;
import com.taller.bookstore.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final OrderMapper orderMapper;

    public OrderServiceImpl(OrderRepository orderRepository,
                            UserRepository userRepository,
                            BookRepository bookRepository,
                            OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    public OrderResponse createOrder(CreateOrderRequest request, String userEmail) {
        User user = findUser(userEmail);
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);

        List<OrderItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CreateOrderItemRequest itemRequest : request.items()) {
            Book book = bookRepository.findDetailedById(itemRequest.bookId())
                    .orElseThrow(() -> new ResourceNotFoundException("book", "id", itemRequest.bookId()));

            if (book.getStock() < itemRequest.quantity()) {
                throw new InsufficientStockException(book.getId(), itemRequest.quantity(), book.getStock());
            }

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setBook(book);
            item.setQuantity(itemRequest.quantity());
            item.setSubtotal(book.getPrice().multiply(BigDecimal.valueOf(itemRequest.quantity())));
            items.add(item);
            total = total.add(item.getSubtotal());
        }

        order.setItems(items);
        order.setTotal(total);
        return orderMapper.toResponse(orderRepository.save(order));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc().stream().map(orderMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByUser(String userEmail) {
        User user = findUser(userEmail);
        return orderRepository.findByUserIdOrderByCreatedAtDesc(user.getId()).stream().map(orderMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id, String userEmail, boolean admin) {
        Order order = findOrder(id);
        validateOrderOwnership(order, userEmail, admin);
        return orderMapper.toResponse(order);
    }

    @Override
    public OrderResponse confirmOrder(Long id) {
        Order order = findOrder(id);
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new InvalidOrderStateException(id, "cannot be confirmed from state " + order.getStatus().name());
        }

        for (OrderItem item : order.getItems()) {
            Book book = item.getBook();
            if (book.getStock() < item.getQuantity()) {
                throw new InsufficientStockException(book.getId(), item.getQuantity(), book.getStock());
            }
            book.setStock(book.getStock() - item.getQuantity());
        }

        order.setStatus(OrderStatus.CONFIRMED);
        return orderMapper.toResponse(orderRepository.save(order));
    }

    @Override
    public OrderResponse cancelOrder(Long id, String userEmail, boolean admin) {
        Order order = findOrder(id);
        validateOrderOwnership(order, userEmail, admin);

        if (order.getStatus() == OrderStatus.CONFIRMED) {
            throw new InvalidOrderStateException(id, "cannot be cancelled because it is already CONFIRMED");
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new InvalidOrderStateException(id, "is already CANCELLED");
        }

        order.setStatus(OrderStatus.CANCELLED);
        return orderMapper.toResponse(orderRepository.save(order));
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("user", "email", email));
    }

    private Order findOrder(Long id) {
        return orderRepository.findDetailedById(id)
                .orElseThrow(() -> new ResourceNotFoundException("order", "id", id));
    }

    private void validateOrderOwnership(Order order, String userEmail, boolean admin) {
        if (!admin && !order.getUser().getEmail().equals(userEmail)) {
            throw new UnauthorizedAccessException("You cannot access orders from another user");
        }
    }
}
