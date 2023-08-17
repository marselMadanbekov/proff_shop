package com.profi_shop.repositories;

import com.profi_shop.model.Category;
import com.profi_shop.model.Product;
import com.profi_shop.model.ProductGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductGroupRepository extends JpaRepository<ProductGroup, Long> {
    @Query("SELECT p FROM ProductGroup p WHERE (:name IS NULL OR p.name = :name) ")
    Page<ProductGroup> findAllByFilters(String name, Pageable pageable);

    List<ProductGroup> findProductGroupByProducts(Product product);
}
