package com.profi_shop.dto;


import com.profi_shop.model.Product;
import com.profi_shop.model.ProductVariation;
import lombok.Data;

import java.util.List;

@Data
public class OrderItemDTO {
    private Long id;
    private Product product;
    private ProductVariation productVariation;
    private List<ProductVariation> availableSizes;
    private Integer quantity;
}
