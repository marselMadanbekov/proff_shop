package com.profi_shop.model;

import com.profi_shop.model.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int total_price;
    private OrderStatus status;
    @ManyToOne
    private Store store;
    @OneToMany
    private List<OrderItem> orderItems;
    @ManyToOne
    private Shipment shipment;
    private Date date;
    @PrePersist
    private void onCreate(){
        date = Date.valueOf(LocalDate.now());
    }
}
