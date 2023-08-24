package com.profi_shop.model.requests;

import lombok.Data;

@Data
public class OrderUpdateRequest {
    private Long orderItemId;
    private Long productVariationId;
}
