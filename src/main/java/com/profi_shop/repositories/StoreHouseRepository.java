package com.profi_shop.repositories;

import com.profi_shop.model.Product;
import com.profi_shop.model.ProductVariation;
import com.profi_shop.model.Store;
import com.profi_shop.model.StoreHouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreHouseRepository extends JpaRepository<StoreHouse, Long> {
    Optional<StoreHouse> findByProductAndStore(ProductVariation product, Store store);

    @Query("SELECT sh.product, sh.quantity FROM StoreHouse sh WHERE sh.store = :store")
    Page<Object[]> findProductsAndQuantitiesByStore(Store store, Pageable pageable);
    List<StoreHouse> findStoreHouseByProduct(ProductVariation product);
}
