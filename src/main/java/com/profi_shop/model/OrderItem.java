package com.profi_shop.model;

import jakarta.persistence.*;
import lombok.Data;
import org.aspectj.weaver.ast.Or;

@Data
@Entity
@Table(name = "orderItems")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity;
    @ManyToOne
    private Product product;
    @ManyToOne
    private ProductVariation productVariation;

    private int price;

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;

        if (orderItem.getProduct().equals(product)) {
            if (orderItem.productVariation == null ^ this.productVariation == null) return false;
            else if (orderItem.productVariation == null) return true;
            return orderItem.getProductVariation().equals(productVariation);
        }else return false;
    }
}
