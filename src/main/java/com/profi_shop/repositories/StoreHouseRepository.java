package com.profi_shop.repositories;

import com.profi_shop.model.Product;
import com.profi_shop.model.ProductVariation;
import com.profi_shop.model.Store;
import com.profi_shop.model.StoreHouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreHouseRepository extends JpaRepository<StoreHouse, Long> {
    Optional<StoreHouse> findByProductAndStore(ProductVariation product, Store store);

    List<StoreHouse> findStoreHouseByProduct(ProductVariation product);
}
