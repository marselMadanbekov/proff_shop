package com.profi_shop.repositories;

import com.profi_shop.model.StoreHouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreHouseRepository extends JpaRepository<StoreHouse, Long> {
}
