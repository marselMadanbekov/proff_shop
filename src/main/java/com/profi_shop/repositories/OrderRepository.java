package com.profi_shop.repositories;

import com.profi_shop.model.Order;
import com.profi_shop.model.OrderItem;
import com.profi_shop.model.Store;
import com.profi_shop.model.enums.OrderStatus;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    Page<Order> findByStatusAndStore(OrderStatus status, Store store, Pageable pageable);

    Page<Order> findByStore(Store store, Pageable pageable);

    Optional<Order> findByOrderItems(OrderItem orderItem);

    @Query("SELECT SUM(o.totalPrice)" +
            " FROM Order o WHERE (o.store = :store)" +
            " and (o.firstname is not NULL) " +
            " and (o.status = :status)" +
            " and (o.date >= :startDate)" +
            " and (o.date <= :endDate)")
    Optional<Integer> calculateTotalOnlineRevenueForStore(OrderStatus status,Store store, Date startDate, Date endDate);

    @Query("SELECT SUM(o.totalPrice)" +
            " FROM Order o WHERE (o.store = :store) " +
            " and (o.firstname is NULL)" +
            " and (o.status = :status)" +
            " and (o.date >= :startDate)" +
            " and (o.date <= :endDate)")
    Optional<Integer> calculateTotalOfflineRevenueForStore(Store store, Date startDate, Date endDate,  OrderStatus status);

    @Query("SELECT COUNT(o) FROM Order o" +
            " WHERE (o.status = :status)" +
            " and (o.store = :store)" +
            " and (o.date >= :startDate)" +
            " and (o.date <= :endDate)")
    Optional<Integer> countOrdersByStatus(OrderStatus status, Store store, Date startDate, Date endDate);
}
