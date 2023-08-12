package com.profi_shop.dto;

import com.profi_shop.model.CartItem;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class CartDTO {
    private Map<Long, Integer> product_quantity = new HashMap<>();
    public void addProduct(Long productId, int quantity){
        product_quantity.put(productId,quantity);
    }
}
