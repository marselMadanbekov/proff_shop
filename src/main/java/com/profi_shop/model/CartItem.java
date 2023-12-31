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

    // 0 - absent, 1 - authenticated, 2 - all, 3 - coupon
    private int stockType;
    private int quantity;

    public int getAmountWithDiscount() {
        if (discount > 0)
            return (int) Math.ceil(product.getPrice() - (product.getPrice() * discount) / 100.0) * quantity;
        return product.getPrice() * quantity;
    }

    public int getTotalAmount(){
        return product.getPrice() * quantity;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;

        if (cartItem.getProduct().equals(product)) {
            if (cartItem.productVariation == null ^ this.productVariation == null) return false;
            else if (cartItem.productVariation == null) return true;
            return cartItem.getProductVariation().equals(productVariation);
        }else return false;
    }
}
