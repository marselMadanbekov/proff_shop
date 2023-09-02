package com.profi_shop.repositories;

import com.profi_shop.model.ProductVariation;
import com.profi_shop.model.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    List<Receipt> findByProductVariation(ProductVariation productVariation);

    @Query("SELECT SUM(r.quantity) " +
            "FROM Receipt r " +
            "WHERE r.productVariation = :productVariation " +
            "AND r.date BETWEEN :startDate AND :endDate")
    Optional<Integer> getTotalQuantityBetweenDates(@Param("productVariation") ProductVariation productVariation,
                                                   @Param("startDate") Date startDate,
                                                   @Param("endDate") Date endDate);
    @Query("SELECT r " +
            "FROM Receipt r " +
            "WHERE r.productVariation = :productVariation " +
            "AND r.date BETWEEN :startDate AND :endDate")
    List<Receipt> getReceiptsByProductVariationBetween(ProductVariation productVariation, Date startDate,Date endDate);
}
