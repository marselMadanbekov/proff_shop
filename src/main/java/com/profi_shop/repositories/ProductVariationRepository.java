package com.profi_shop.repositories;

import com.profi_shop.model.Product;
import com.profi_shop.model.ProductVariation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductVariationRepository extends JpaRepository<ProductVariation, Long> {
    List<ProductVariation> findByParent(Product product);
}
