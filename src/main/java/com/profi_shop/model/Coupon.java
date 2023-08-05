package com.profi_shop.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDate;

@Data
@Entity
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date start_date;
    private Date end_date;
    private int discount;
    private String activation_code;
    @ManyToOne
    private User owner;
    @ManyToOne
    private CouponTemplate parent;

    @PrePersist
    private void onCreate(){
        start_date = Date.valueOf(LocalDate.now());
    }
}
