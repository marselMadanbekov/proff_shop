package com.profi_shop.services;

import com.profi_shop.exceptions.SearchException;
import com.profi_shop.model.CouponTemplate;
import com.profi_shop.repositories.CouponTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CouponTemplateService {
    private final CouponTemplateRepository couponTemplateRepository;

    @Autowired
    public CouponTemplateService(CouponTemplateRepository couponTemplateRepository) {
        this.couponTemplateRepository = couponTemplateRepository;
    }

    public void createTemplate(CouponTemplate request){
        CouponTemplate couponTemplate = new CouponTemplate();
        couponTemplate.setDuration(request.getDuration());
        couponTemplate.setDiscount(request.getDiscount());
        couponTemplate.setMinAmount(request.getMinAmount());

        couponTemplateRepository.save(couponTemplate);
    }

    public Page<CouponTemplate> getPagedTemplates(Integer page) {
        Pageable pageable = PageRequest.of(page,10);
        return couponTemplateRepository.findAll(pageable);
    }

    public CouponTemplate getCouponTemplateById(Long couponTid){
        return couponTemplateRepository.findById(couponTid).orElseThrow(() -> new SearchException(SearchException.COUPON_TEMPLATE_NOT_FOUND));
    }
    public void deleteCouponTemplateById(Long id) {
        couponTemplateRepository.delete(getCouponTemplateById(id));
    }
}
