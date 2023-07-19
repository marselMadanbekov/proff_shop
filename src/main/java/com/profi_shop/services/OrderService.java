package com.profi_shop.services;

import com.profi_shop.model.Order;
import com.profi_shop.model.requests.OrderRequest;
import com.profi_shop.repositories.OrderItemRepository;
import com.profi_shop.repositories.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public Order createOrder(OrderRequest orderRequest){
        Order order = new Order();
        return new Order();
    }
}
