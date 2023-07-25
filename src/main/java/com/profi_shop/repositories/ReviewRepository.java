package com.profi_shop.repositories;

import com.profi_shop.model.Product;
import com.profi_shop.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT AVG(r.mark) FROM Review r WHERE r.product = :product")
    Optional<Double> findAverageMarkByProduct(Product product);
}
