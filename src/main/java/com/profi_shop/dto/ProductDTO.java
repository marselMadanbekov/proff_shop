package com.profi_shop.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private int price;
    private List<String> photos;
    private int rating;
    private String description;
    private int discount;
    private int newPrice;
    private int oldPrice;
}
