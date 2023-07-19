package com.profi_shop.dto;

import lombok.Data;

@Data
public class StockDTO {
    private Long id;
    private int quantity;
    private StoreDTO store;
    private ProductDTO product;
}
