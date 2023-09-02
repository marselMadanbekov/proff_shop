package com.profi_shop.repositories;

import com.profi_shop.model.OrderItem;
import com.profi_shop.model.Product;
import com.profi_shop.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
