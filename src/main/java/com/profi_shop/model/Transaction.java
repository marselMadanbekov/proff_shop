package com.profi_shop.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDate;

@Entity
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer amount;
    @ManyToOne
    private Store sender;
    @ManyToOne
    private Store target;
    private Date date;

    @PrePersist
    private void onCreate(){
        this.date = Date.valueOf(LocalDate.now());
    }
}
