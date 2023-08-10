package com.profi_shop.services.facade;

import com.profi_shop.dto.CouponTemplateDTO;
import com.profi_shop.model.CouponTemplate;
import com.profi_shop.repositories.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CouponTemplateFacade {
    private final CouponRepository couponRepository;

    @Autowired
    public CouponTemplateFacade(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public CouponTemplateDTO couponTemplateToDTO(CouponTemplate couponTemplate){
        CouponTemplateDTO dto = new CouponTemplateDTO();
        dto.setId(couponTemplate.getId());
        dto.setDiscount(couponTemplate.getDiscount());
        dto.setMinAmount(couponTemplate.getMinAmount());
        dto.setDuration(couponTemplate.getDuration());
        dto.setCreated_date(couponTemplate.getCreated_date());
        dto.setTotal_generated(couponRepository.countByParent(couponTemplate));
        return dto;
    }

    public Page<CouponTemplateDTO> mapToCouponTemplateDTOPage(Page<CouponTemplate> couponTemplateDTOPage) {
        List<CouponTemplateDTO> couponTemplateDTOList = couponTemplateDTOPage.getContent()
                .stream()
                .map(this::couponTemplateToDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(couponTemplateDTOList, couponTemplateDTOPage.getPageable(), couponTemplateDTOPage.getTotalElements());
    }
}
