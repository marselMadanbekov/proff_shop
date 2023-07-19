package com.profi_shop.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class StoreHouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Store store;
    @ManyToOne
    private Product product;
    private int quantity;
}
