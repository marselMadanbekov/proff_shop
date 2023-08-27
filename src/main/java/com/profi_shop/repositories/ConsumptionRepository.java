package com.profi_shop.repositories;

import com.profi_shop.model.Consumption;
import com.profi_shop.model.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumptionRepository extends JpaRepository<Consumption,Long> {
    Page<Consumption> findByStore(Store store, Pageable pageable);
}
