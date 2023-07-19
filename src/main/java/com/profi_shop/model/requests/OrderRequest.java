package com.profi_shop.model.requests;

import lombok.Data;

import java.util.Map;

@Data
public class OrderRequest {
    private Long shipmentId;
    private String streetAddress;
    private String customerFirstname;
    private String customerLastname;
    private String customerPhone;
    private String customerEmail;
    private Map<Long,Integer> orderItems;
}
