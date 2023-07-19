package com.profi_shop.dto;

import com.profi_shop.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private int prime_cost;
    private int price;
    private String sku;
    private String description;
    private Category category;

    public ProductDTO(){};
}
