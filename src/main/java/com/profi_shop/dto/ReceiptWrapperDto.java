package com.profi_shop.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReceiptWrapperDto {
    private List<ReceiptDTO> receipts;
    private String monthName;

    public ReceiptWrapperDto(List<ReceiptDTO> receipts, String monthName) {
        this.receipts = receipts;
        this.monthName = monthName;
    }
}
