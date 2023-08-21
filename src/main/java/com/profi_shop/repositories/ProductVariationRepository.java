package com.profi_shop.repositories;

import com.profi_shop.model.Product;
import com.profi_shop.model.ProductVariation;
import com.profi_shop.model.enums.ProductSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductVariationRepository extends JpaRepository<ProductVariation, Long> {
    List<ProductVariation> findByParent(Product product);
    Optional<ProductVariation> findByParentAndProductSize(Product product, ProductSize size);
}
