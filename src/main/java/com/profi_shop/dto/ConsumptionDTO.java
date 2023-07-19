package com.profi_shop.dto;

import lombok.Data;

@Data
public class ConsumptionDTO {
    private Long id;
    private String description;
    private int amount;
    private StoreDTO store;
}
