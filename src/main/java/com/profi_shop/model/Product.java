package com.profi_shop.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Fetch;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int prime_cost;
    private int price;
    private String sku;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "photos", joinColumns = @JoinColumn(name = "product_id"))
    private List<String> photos = new ArrayList<>();
    private String description;
    @ManyToOne
    private Category category;
}
