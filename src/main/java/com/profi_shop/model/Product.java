package com.profi_shop.model;

import com.profi_shop.model.enums.ProductSize;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private String color;

    private Date create_date;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "product_specification", joinColumns = @JoinColumn(name = "product_id"))
    @MapKeyColumn(name = "spec_key")
    @Column(name = "spec_value")
    private Map<String,String> specifications = new HashMap<>();
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "photos", joinColumns = @JoinColumn(name = "product_id"))
    private List<String> photos = new ArrayList<>();
    private String description;
    @ManyToOne
    private Category category;

    @PrePersist
    private void creating(){
        this.create_date = Date.valueOf(LocalDate.now());
    }
    public void addPhoto(String photo) {
        photos.add(photo);
    }

    public void removePhoto(String photo) {
        photos.remove(photo);
    }

    public void addSpecification(String key, String value){
        this.specifications.put(key, value);
    }

    public void removeSpecification(String key) {
        this.specifications.remove(key);
    }
}
