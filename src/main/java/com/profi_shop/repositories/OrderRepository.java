package com.profi_shop.repositories;

import com.profi_shop.model.*;
import com.profi_shop.model.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    Page<Order> findByStatusAndStore(OrderStatus status, Store store, Pageable pageable);

    Page<Order> findByStore(Store store, Pageable pageable);

    Optional<Order> findByOrderItems(OrderItem orderItem);

    @Query("SELECT SUM(o.totalPrice)" +
            " FROM Order o WHERE (o.store = :store)" +
            " and (o.firstname IS NOT NULL) " +
            " and (o.status = :status)" +
            " and (o.shipment IS NULL)" +
            " and (o.date >= :startDate)" +
            " and (o.date <= :endDate)")
    Optional<Integer> calculateTotalOnlineRevenueForStoreLessShipment(OrderStatus status,Store store, Date startDate, Date endDate);

    @Query("SELECT SUM(o.totalPrice - o.shipment.cost)" +
            " FROM Order o WHERE (o.store = :store)" +
            " and (o.firstname IS NOT NULL) " +
            " and (o.status = :status)" +
            " and (o.shipment IS NOT NULL)" +
            " and (o.date >= :startDate)" +
            " and (o.date <= :endDate)")
    Optional<Integer> calculateTotalOnlineRevenueForStoreMinusShipment(OrderStatus status,Store store, Date startDate, Date endDate);


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

    @Query("SELECT o FROM Order o WHERE (o.store = :store)" +
            " and (o.firstname IS NOT NULL) " +
            " and (o.status = :status)")
    Page<Order> getPageOnlineSales(Store store, Pageable pageable, OrderStatus status);
    @Query("SELECT o FROM Order o WHERE (o.store = :store)" +
            " and (o.firstname IS NULL) " +
            " and (o.status = :status)")
    Page<Order> getPageOfflineSales(Store store, Pageable pageable, OrderStatus status);

    @Query("SELECT FUNCTION('MONTH', o.date) AS month, SUM(oi.quantity) AS sales " +
            "FROM Order o " +
            "JOIN o.orderItems oi " +
            "WHERE oi.product = :product " +
            "AND o.date >= :date " +
            "AND o.status = :status " +
            "GROUP BY FUNCTION('MONTH', o.date)")
    List<Object[]> getSalesDataForProductLast4Months(@Param("product") Product product, @Param("date") Date date, OrderStatus status);

    @Query("SELECT SUM(oi.quantity) " +
            "FROM Order o " +
            "JOIN o.orderItems oi " +
            "WHERE oi.productVariation = :product " +
            "AND o.date >= :startDate " +
            "AND o.date <= :endDate " +
            "AND o.status = :status")
    Optional<Integer> getCoundSoldProductBetweenDates(@Param("product") ProductVariation product, @Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("status") OrderStatus status);


    @Query("SELECT SUM(oi.price) AS totalSales, SUM(oi.quantity) AS totalQuantity " +
            "FROM Order o " +
            "JOIN o.orderItems oi " +
            "WHERE oi.productVariation = :product " +
            "AND o.date >= :startDate " +
            "AND o.date <= :endDate " +
            "AND o.status = :status")
    Map<String, Long> getTotalSalesAndQuantityForProductBetweenDates(@Param("product") ProductVariation product,
                                                                        @Param("startDate") Date startDate,
                                                                        @Param("endDate") Date endDate,
                                                                        @Param("status") OrderStatus status);
    Order findByCoupon(Coupon coupon);

    List<Order> findTop10ByUserOrderByDateDesc(User user);
}
