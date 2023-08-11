package com.profi_shop.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "orderItems")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity;
    @OneToOne
    private Product product;

    private int price;
}
