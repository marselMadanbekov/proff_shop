package com.profi_shop.dto;

import com.profi_shop.model.enums.OrderStatus;
import lombok.Data;

import java.sql.Date;

@Data
public class OrderDTO {
    private Long id;
    private int total_price;
    private OrderStatus status;
    private Date date;
}
