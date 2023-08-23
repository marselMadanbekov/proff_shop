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
    Page<Product> findAllByName(String name, Pageable pageable);

    List<Product> findAllByCategory(Category category);

    Page<Product> findAllByCategoryAndColor(Pageable pageable, Category category, String color);
    @Query(value = "SELECT DISTINCT tags FROM tags LIMIT 10", nativeQuery = true)
    List<String> findTop10DistinctTags();
    @Query(value = "SELECT DISTINCT brand FROM products LIMIT 10", nativeQuery = true)
    List<String> findTop10DistinctBrands();

    @Query("SELECT DISTINCT p FROM Product p " +
            "WHERE (:category IS NULL OR (p.category = :category OR p.category.parent = :category)) " +
            "AND (:query IS NULL OR (p.name LIKE %:query% OR EXISTS (SELECT 1 FROM ProductVariation pv WHERE pv.parent = p AND pv.sku = :query))) " +
            "AND (:brand IS NULL OR (p.brand = :brand)) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
            "AND (:size IS NULL OR EXISTS (SELECT 1 FROM ProductVariation pv WHERE pv.parent = p AND pv.size = :size))" +
            "AND (:tag IS NULL OR :tag MEMBER OF p.tags)")
    Page<Product> findAllByFilters(
            Category category,
            String query,
            Integer minPrice,
            Integer maxPrice,
            String size,
            String tag,
            String brand,
            Pageable pageable
    );
}
