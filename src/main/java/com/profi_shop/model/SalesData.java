package com.profi_shop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
public class SalesData {
    private String month;
    private Integer sales;
}
