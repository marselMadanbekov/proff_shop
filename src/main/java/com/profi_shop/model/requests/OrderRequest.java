package com.profi_shop.model.requests;

import lombok.Data;

import java.util.Map;

@Data
public class OrderRequest {
    private String state;
    private String town;
    private String streetAddress;
    private String firstname;
    private String lastname;
    private String phone_number;
    private String email;
    private boolean toShip;
}
