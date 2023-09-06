package com.profi_shop.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class TodayDeals {
    private Long id;
    private String name;
    private String photo;
    private int rating;
    private int discount;
    private Date startDate;
    private Date endDate;
    private int oldPrice;
    private int newPrice;
}
