package com.profi_shop.dto;

import com.profi_shop.model.CartItem;
import lombok.Data;

import java.util.List;

@Data
public class CartDTO {
    private Long id;
    private UserDTO user;
    private List<CartItem> products;
}
