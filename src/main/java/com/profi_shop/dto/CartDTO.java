package com.profi_shop.dto;

import com.profi_shop.model.CartItem;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class CartDTO {
    private Long couponId;
    private List<CartItemDTO> cartItems = new ArrayList<>();
    public void addItem(CartItemDTO item){
        this.cartItems.add(item);
    }
}
