package com.profi_shop.repositories;

import com.profi_shop.model.Coupon;
import com.profi_shop.model.CouponTemplate;
import com.profi_shop.model.Order;
import com.profi_shop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    @Query("SELECT COUNT(c) FROM Coupon c WHERE c.parent = :parent")
    Integer countByParent(@Param("parent") CouponTemplate parent);

    Optional<Coupon> findByRoot(Order order);

    Optional<Coupon> findByActivationCode(String couponCode);

    List<Coupon> findByOwner(User user);
}
