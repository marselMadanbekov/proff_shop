package com.profi_shop.model;

import com.profi_shop.exceptions.ExistException;
import com.profi_shop.model.enums.OrderStatus;
import com.profi_shop.model.enums.ShipmentType;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;
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
    private int totalPrice;
    private String firstname;
    private String lastname;
    private String email;
    private String phone_number;
    private OrderStatus status;
    private ShipmentType shipmentType;
    @OneToOne
    private Coupon coupon;
    @ManyToOne
    private Store store;
    @OneToMany
    private List<OrderItem> orderItems = new ArrayList<>();
    @ManyToOne
    private Shipment shipment;

    @ManyToOne
    private User user;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp date;
    @PrePersist
    private void onCreate(){
        date = new Timestamp(System.currentTimeMillis());
    }

    public void addOrderItem(OrderItem orderItem) throws ExistException {
        if(orderItems.contains(orderItem))
            throw new ExistException(ExistException.ORDER_ITEM_EXISTS);
        orderItems.add(orderItem);
    }

    public void removeOrderItem(OrderItem orderItem) {
        this.orderItems.removeIf(orderItem1 -> orderItem1.equals(orderItem));
    }
}
