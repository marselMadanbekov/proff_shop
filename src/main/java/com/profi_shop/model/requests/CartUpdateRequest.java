package com.profi_shop.model.requests;

import lombok.Data;

@Data
public class CartUpdateRequest {
    private Long productId;
    private Long productVariationId;
    private Integer newQuantity;
}
