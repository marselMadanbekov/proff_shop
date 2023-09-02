package com.profi_shop.model.requests;

import lombok.Data;

@Data
public class ProductEditRequest {
    private Long targetProductId;
    private String name;
    private Integer primeCost;
    private Integer price;
    private String brand;
    private String color;
    private Long categoryId;
    private String description;
}
