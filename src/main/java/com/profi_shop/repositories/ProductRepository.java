package com.profi_shop.repositories;

import com.profi_shop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findAllBySku(String sku);
    List<Product> findAllByName(String name);
}
