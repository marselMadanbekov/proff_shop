package com.profi_shop.repositories;

import com.profi_shop.model.Consumption;
import com.profi_shop.model.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.Optional;

@Repository
public interface ConsumptionRepository extends JpaRepository<Consumption,Long> {
    Page<Consumption> findByStore(Store store, Pageable pageable);
    @Query("SELECT SUM(c.amount) FROM Consumption c WHERE c.store = :store " +
            "AND c.date >= :startDate " +
            "AND c.date <= :endDate")
    Optional<Integer> getTotalConsumptionAmountForStore(Store store, Date startDate, Date endDate);
}
