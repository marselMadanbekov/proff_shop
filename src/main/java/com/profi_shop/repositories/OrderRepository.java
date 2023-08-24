package com.profi_shop.repositories;

import com.profi_shop.model.Order;
import com.profi_shop.model.OrderItem;
import com.profi_shop.model.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    Optional<Order> findByOrderItems(OrderItem orderItem);
}
