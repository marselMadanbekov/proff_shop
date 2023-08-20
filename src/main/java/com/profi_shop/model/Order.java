package com.profi_shop.model;

import com.profi_shop.model.enums.OrderStatus;
import com.profi_shop.model.enums.ShipmentType;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int total_price;
    private String firstname;
    private String lastname;
    private String email;
    private String phone_number;
    private OrderStatus status;
    private ShipmentType shipmentType;
    @ManyToOne
    private Store store;
    @OneToMany
    private List<OrderItem> orderItems = new ArrayList<>();
    @ManyToOne
    private Shipment shipment;

    @ManyToOne
    private User user;
    private Date date;
    @PrePersist
    private void onCreate(){
        date = Date.valueOf(LocalDate.now());
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
    }
}
