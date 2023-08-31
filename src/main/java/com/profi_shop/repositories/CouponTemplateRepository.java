package com.profi_shop.repositories;

import com.profi_shop.model.CouponTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CouponTemplateRepository extends JpaRepository<CouponTemplate, Long> {
    Optional<CouponTemplate> findTopByMinAmountLessThanOrderByMinAmountDesc(int minAmount);
}
