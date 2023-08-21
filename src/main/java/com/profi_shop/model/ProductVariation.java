package com.profi_shop.model;

import com.profi_shop.model.enums.ProductSize;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ProductVariation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private ProductSize productSize;
    @ManyToOne
    private Product parent;

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductVariation productVariation = (ProductVariation) o;
        return this.productSize.equals(productVariation.getProductSize());
    }
}
