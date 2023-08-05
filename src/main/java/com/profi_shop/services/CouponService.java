package com.profi_shop.services;

import com.profi_shop.model.Coupon;
import com.profi_shop.model.CouponTemplate;
import com.profi_shop.model.User;
import com.profi_shop.repositories.CouponRepository;
import com.profi_shop.settings.Templates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;

@Service
public class CouponService {
    private final CouponRepository couponRepository;

    @Autowired
    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public void createCoupon(User user, CouponTemplate couponTemplate){
        Coupon coupon = new Coupon();
        coupon.setEnd_date(Date.valueOf(LocalDate.now().plusDays(couponTemplate.getDuration())));
        coupon.setOwner(user);
        coupon.setParent(couponTemplate);
        coupon.setDiscount(coupon.getDiscount());
        couponRepository.save(coupon);

    }


    private void setActivationCodeToCoupon(Coupon coupon){
        String activationCode = Templates.COUPON_PREFIX + coupon.getParent().getId() + "-" + coupon.getId();
        coupon.setActivation_code(activationCode);
        couponRepository.save(coupon);
    }
}
