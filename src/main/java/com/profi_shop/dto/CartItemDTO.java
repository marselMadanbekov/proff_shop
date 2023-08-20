package com.profi_shop.dto;

import lombok.Data;

@Data
public class CartItemDTO {
    private Long productId;
    private int quantity;
    private Integer size;
}
