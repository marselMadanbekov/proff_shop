package com.profi_shop.dto;

import lombok.Data;

@Data
public class StoreAnalyticsDTO {
    private Integer onlineSales;
    private Integer offlineSales;
    private Integer canceledOrders;
    private Integer consumptionsAmount;
    private Integer transactionsAmount;
}
