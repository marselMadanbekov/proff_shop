package com.profi_shop.model.requests;

import lombok.Data;

import java.util.Map;

@Data
public class ProductGroupRequest {
    private String name;
    private Map<Long, Boolean> members;
}
