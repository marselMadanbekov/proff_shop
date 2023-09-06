package com.profi_shop.model;

import com.profi_shop.model.enums.OrderStatus;
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
    private boolean used;
    private String activationCode;
    @ManyToOne
    private User owner;

    @OneToOne
    private Order root;
    @ManyToOne
    private CouponTemplate parent;

    @PrePersist
    private void onCreate(){
        start_date = Date.valueOf(LocalDate.now());
    }

    public boolean isActive() {
        if (root.getStatus().ordinal() >= OrderStatus.PAID.ordinal()) {
            LocalDate currentDate = LocalDate.now();
            return start_date.toLocalDate().compareTo(currentDate) <= 0 &&
                    end_date.toLocalDate().compareTo(currentDate) >= 0;
        }else return false;
    }

    public boolean isActiveRoot(){
        return root.getStatus().ordinal() >= OrderStatus.PAID.ordinal();
    }
}
