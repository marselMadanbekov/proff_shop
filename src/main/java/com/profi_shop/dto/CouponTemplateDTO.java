package com.profi_shop.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class CouponTemplateDTO {
    private Long id;
    private int minAmount;
    private int duration;
    private int discount;
    private int total_generated;
    private Date created_date;
}
