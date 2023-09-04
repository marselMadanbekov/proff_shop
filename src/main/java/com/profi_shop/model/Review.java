package com.profi_shop.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDate;

@Data
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Product product;
    private int mark;
    private String username;
    private boolean approved;
    private String text;

    private Date date;
    @PrePersist
    public void onCreate(){
        this.date = Date.valueOf(LocalDate.now());
    }
}
