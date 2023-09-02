package com.profi_shop.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class ReceiptDTO {
    private Long id;
    private String storeTown;
    private String productName;
    private String productSize;
    private int quantity;
    private String date;
    private String actorInfo;
}
