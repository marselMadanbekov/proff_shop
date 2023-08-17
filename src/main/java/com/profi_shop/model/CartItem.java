package com.profi_shop.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Product product;
    @ManyToOne
    private ProductVariation productVariation;
    private int discount;
    private int quantity;
    public int getAmount(){
        if(discount > 0)    return (int) Math.ceil(product.getPrice() - (product.getPrice() * discount) / 100.0) * quantity;
        return product.getPrice() * quantity;
    }
}
