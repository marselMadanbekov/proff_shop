package com.profi_shop.dto;

import lombok.Data;

@Data
public class StoreDTO {
    private Long id;
    private String name;
    private String town;
    private String phone_number;
    private UserDTO admin;
}
