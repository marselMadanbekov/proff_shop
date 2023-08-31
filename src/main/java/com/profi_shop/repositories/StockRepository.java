package com.profi_shop.repositories;

import com.profi_shop.model.Product;
import com.profi_shop.model.Stock;
import com.profi_shop.model.StoreHouse;
import com.profi_shop.model.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    Page<Stock> findActiveStocks(@Param("currentDate") LocalDate currentDate,Pageable pageable);
    @Query("SELECT s FROM Stock s WHERE s.endDate < :currentDate AND s.startDate > :currentDate")
    Page<Stock> findInActiveStocks(@Param("currentDate") LocalDate currentDate,Pageable pageable);

    @Query("SELECT p FROM Stock s JOIN s.participants p WHERE s.id = :stockId")
    Page<Product> findParticipantsForStock(@Param("stockId") Long stockId, Pageable pageable);
}
