package com.profi_shop.model;

import jakarta.persistence.*;
import lombok.Data;

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

    public Cart(){}
    public Cart(User user){
        this.user = user;
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
                item.setAmount(quantity * product.getPrice());
            }
        }
    }

    public int cartAmount(){
        return cartItems.stream()
                .mapToInt(CartItem::getAmount) // Получаем поток числовых значений
                .sum();
    }
}
