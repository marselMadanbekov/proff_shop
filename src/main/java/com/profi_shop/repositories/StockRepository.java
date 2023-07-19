package com.profi_shop.repositories;

import com.profi_shop.model.Product;
import com.profi_shop.model.Stock;
import com.profi_shop.model.StoreHouse;
import com.profi_shop.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock,Long> {

}
