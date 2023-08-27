package com.profi_shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductQuantityDTO {
    private Long id;
    private String photo;
    private String name;
    private Integer price;
    private String size;
    private int quantity;
}
