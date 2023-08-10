package com.profi_shop.dto;

import com.profi_shop.model.User;
import lombok.Data;

import java.util.List;

@Data
public class WishListDTO {
    private Long id;
    private User user;
    private List<ProductDetailsDTO> products;
}
