package com.profi_shop.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDate;

@Data
@Entity
public class CouponTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int minAmount;
    private int duration;
    private int discount;
    private Date created_date;

    @PrePersist
    private void onCreate(){
        this.created_date = Date.valueOf(LocalDate.now());
    }
}
