package com.profi_shop.model;

import com.profi_shop.exceptions.ExistException;
import com.profi_shop.model.enums.ProductSize;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;
    @OneToMany
    private List<CartItem> cartItems = new ArrayList<>();

    private Date last_update;

    @OneToOne
    private Coupon coupon;

    @PrePersist
    private void onCreate(){
        this.last_update = Date.valueOf(LocalDate.now());
    }

    public void updated(){
        this.last_update = Date.valueOf(LocalDate.now());
    }

    public Cart(){
        updated();
    }
    public Cart(User user){
        this.user = user;
        updated();
    }

    public void addItemToCart(CartItem cartItem) throws ExistException {
        if(!cartItems.contains(cartItem))
            this.cartItems.add(cartItem);
        else{
            throw new ExistException(ExistException.CART_ITEM_EXIST);
        }
    }

    public void removeProduct(Product product) {
        cartItems.removeIf(cartItem -> cartItem.getProduct().equals(product) && cartItem.getProductVariation() == null);
    }

    public void removeProductVariation(ProductVariation productVariation){
        cartItems.removeIf(cartItem -> {
            if (cartItem.getProductVariation() == null ^ productVariation == null) return false;
            else if(cartItem.getProductVariation() == null) return true;
            return cartItem.getProductVariation().equals(productVariation);
        });
    }

    public void quantityUp(Product product, int quantity){
        for(CartItem item : cartItems){
            if(item.getProduct().equals(product)){
                item.setQuantity(quantity);
            }
        }
    }

    public int cartAmount(){
        return cartItems.stream()
                .mapToInt(CartItem::getTotalAmount) // Получаем поток числовых значений
                .sum();
    }

    public int cartAmountWithDiscount(){
        return cartItems.stream()
                .mapToInt(CartItem::getAmountWithDiscount) // Получаем поток числовых значений
                .sum();
    }

    public boolean isCouponApplicable(){
        for(CartItem cartItem: cartItems){
            if(cartItem.getDiscount() == 0) return true;
        }
        return false;
    }
}
