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
    private ProductVariation product;
    private int quantity;

    public StoreHouse(){}

    public StoreHouse(ProductVariation product, Store store){
        this.product = product;
        this.store = store;
    }

    public void quantityDown(int quantity) {
        this.quantity -= quantity;
    }

    public void quantityUp(int quantity){
        this.quantity += quantity;
    }
}
