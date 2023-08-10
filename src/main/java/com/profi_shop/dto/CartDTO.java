package com.profi_shop.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartDTO {
    private Long id;
    private UserDTO user;
    private List<ProductDetailsDTO> products;
}
