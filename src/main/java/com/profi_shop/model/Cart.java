package com.profi_shop.model;

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

    @PrePersist
    private void onCreate(){
        this.last_update = Date.valueOf(LocalDate.now());
    }

    public void updated(){
        this.last_update = Date.valueOf(LocalDate.now());
    }

    public boolean needForUpdating(){
        return LocalDate.now().minusDays(1).isAfter(last_update.toLocalDate());
    }
    public Cart(){
        updated();
    }
    public Cart(User user){
        this.user = user;
        updated();
    }

    public void addItemToCart(CartItem cartItem){
        this.cartItems.add(cartItem);
    }

    public void removeProduct(Product product) {
        cartItems.removeIf(cartItem -> cartItem.getProduct().equals(product));
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
                .mapToInt(CartItem::getAmount) // Получаем поток числовых значений
                .sum();
    }
}
