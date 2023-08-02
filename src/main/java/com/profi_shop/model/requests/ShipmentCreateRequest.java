package com.profi_shop.model.requests;

import lombok.Data;

@Data
public class ShipmentCreateRequest {
    private String state;
    private String town;
    private int cost;
}
