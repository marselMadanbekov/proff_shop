package com.profi_shop.repositories;

import com.profi_shop.model.Category;
import com.profi_shop.model.Product;
import com.profi_shop.model.enums.ProductSize;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    Page<Product> findAllBySku(String sku,Pageable pageable);
    Page<Product> findAllByName(String name, Pageable pageable);

    List<Product> findAllByCategory(Category category);

    Page<Product> findAllByCategoryAndColorAndSize(Pageable pageable, Category category, String color, ProductSize productSize);

    @Query("SELECT p FROM Product p " +
            "WHERE (:category IS NULL OR p.category = :category) " +
            "AND (:color IS NULL OR p.color = :color) " +
            "AND (:query IS NULL OR p.name = :query) " +
            "AND (:productSize IS NULL OR p.size = :productSize) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice)")
    Page<Product> findAllByFilters(
            Category category,
            String color,
            String query,
            ProductSize productSize,
            Integer minPrice,
            Integer maxPrice,
            Pageable pageable
    );
}
