package com.profi_shop.dto;

import com.profi_shop.model.*;
import com.profi_shop.model.enums.OrderStatus;
import com.profi_shop.model.enums.ShipmentType;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderDetailsDTO {
    private Long id;
    private int totalPrice;
    private String firstname;
    private String lastname;
    private String email;
    private String phone_number;
    private OrderStatus status;
    private ShipmentType shipmentType;
    private Store store;
    private Coupon coupon;
    private List<OrderItemDTO> orderItems = new ArrayList<>();
    private Shipment shipment;
    private Timestamp date;
}
