package com.profi_shop.model;

import com.profi_shop.model.enums.StockType;
import jakarta.persistence.*;
import lombok.Data;

import java.util.*;

@Data
@Entity
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date startDate;
    private Date endDate;
    private StockType type;
    private int discount;
    @OneToMany(fetch = FetchType.EAGER)
    private List<Category> categories = new ArrayList<>();
    @OneToMany(fetch = FetchType.EAGER)
    private List<Product> participants = new ArrayList<>();


    public void addCategory(Category category){
        categories.add(category);
    }

    public void addProduct(Product product){
        participants.add(product);
    }
    public void addAllParticipants(List<Product> products){
        participants.addAll(products);
    }
}
