package com.profi_shop.model;

import com.profi_shop.model.enums.StockType;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Data
@Entity
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date startDate;
    private Date endDate;
    private StockType type;
    private boolean for_authenticated;
    private int discount;
    private Date createDate;
    @OneToMany(fetch = FetchType.EAGER)
    private List<Category> categories = new ArrayList<>();
    @OneToMany(fetch = FetchType.EAGER)
    private List<Product> participants = new ArrayList<>();

    @PrePersist
    private void onCreate(){
        this.createDate = Date.valueOf(LocalDate.now());
    }

    public void addCategory(Category category){
        categories.add(category);
    }

    public void addProduct(Product product){
        participants.add(product);
    }
    public void addAllParticipants(List<Product> products){
        participants.addAll(products);
    }

    public boolean isActive(){
        return startDate.before(Date.valueOf(LocalDate.now())) && endDate.after(Date.valueOf(LocalDate.now()));
    }

    public void removeParticipant(Product product) {
        this.getParticipants().remove(product);
    }
}
