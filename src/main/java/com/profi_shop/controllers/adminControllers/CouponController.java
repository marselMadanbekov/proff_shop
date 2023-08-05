package com.profi_shop.controllers.adminControllers;

import com.profi_shop.dto.CouponTemplateDTO;
import com.profi_shop.model.CouponTemplate;
import com.profi_shop.services.CouponTemplateService;
import com.profi_shop.services.facade.CouponTemplateFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class CouponController {
    private final CouponTemplateService couponTemplateService;
    private final CouponTemplateFacade couponTemplateFacade;

    @Autowired
    public CouponController(CouponTemplateService couponTemplateService, CouponTemplateFacade couponTemplateFacade) {
        this.couponTemplateService = couponTemplateService;
        this.couponTemplateFacade = couponTemplateFacade;
    }

    @GetMapping("/coupons")
    public String coupons(@RequestParam(value = "page", required = false)Optional<Integer> page,
                          Model model){
        Page<CouponTemplateDTO> coupons = couponTemplateFacade.mapToCouponTemplateDTOPage(couponTemplateService.getPagedTemplates(page.orElse(0)));
        model.addAttribute("coupons", coupons);
        return "admin/coupon/coupon";
    }
    @GetMapping("/couponT/delete")
    public ResponseEntity<String> coupons(@RequestParam("couponTid")Long id){
        couponTemplateService.deleteCouponTemplateById(id);
        return new ResponseEntity<>("successful", HttpStatus.OK);
    }

    @PostMapping("create-couponT")
    public String createCoupon(CouponTemplate couponTemplate){
        couponTemplateService.createTemplate(couponTemplate);
        return "redirect:/admin/coupons";
    }

}
