package com.taller.bookstore.service;

import com.taller.bookstore.dto.request.CreateOrderRequest;
import com.taller.bookstore.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse createOrder(CreateOrderRequest request, String userEmail);

    List<OrderResponse> getAllOrders();

    List<OrderResponse> getOrdersByUser(String userEmail);

    OrderResponse getOrderById(Long id, String userEmail, boolean admin);

    OrderResponse confirmOrder(Long id);

    OrderResponse cancelOrder(Long id, String userEmail, boolean admin);
}
