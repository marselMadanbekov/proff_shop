package com.profi_shop.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class ProductGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @OneToMany
    private List<Product> products = new ArrayList<>();

    private Date create_date;

    @PrePersist
    public void onCreate(){
        this.create_date = Date.valueOf(LocalDate.now());
    }

    public void addProduct(Product product){
        this.products.add(product);
    }

    public void removeProduct(Product product) {
        this.products.remove(product);
    }
}
