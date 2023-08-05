package com.profi_shop.model.requests;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class StockRequest {
    private Date startDate;
    private Date endDate;
    private Integer discount;
    private Integer type;
    private Boolean authenticated;
    private Map<Long, Boolean> participants;
}
