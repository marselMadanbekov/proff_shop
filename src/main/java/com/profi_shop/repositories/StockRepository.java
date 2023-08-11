package com.profi_shop.repositories;

import com.profi_shop.model.Product;
import com.profi_shop.model.Stock;
import com.profi_shop.model.StoreHouse;
import com.profi_shop.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock,Long> {
    Optional<Stock> findByParticipants(Product product);
    @Query("SELECT s FROM Stock s WHERE s.discount = (SELECT MAX(s2.discount) FROM Stock s2)")
    Optional<Stock> findStockWithMaxDiscount();

    @Query("SELECT s FROM Stock s WHERE s.endDate > :currentDate AND s.startDate < :currentDate")
    List<Stock> findActiveStocks(@Param("currentDate") LocalDate currentDate);
    @Query("SELECT s FROM Stock s WHERE (s.endDate > :currentDate AND s.startDate < :currentDate) AND s.active = false")
    List<Stock> findToActiveStocks(@Param("currentDate") LocalDate currentDate);
    @Query("SELECT s FROM Stock s WHERE (s.endDate < :currentDate OR s.startDate > :currentDate) AND s.active = true")
    List<Stock> findDisActiveStocks(@Param("currentDate") LocalDate currentDate);

}
