package com.profi_shop.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDate;

@Data
@Entity
public class Receipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date date;
    @ManyToOne
    private ProductVariation productVariation;
    @ManyToOne
    private Store store;
    private String actorInfo;
    private int quantity;

    @PrePersist
    private void onCreate(){
        this.date = Date.valueOf(LocalDate.now());
    }
}
