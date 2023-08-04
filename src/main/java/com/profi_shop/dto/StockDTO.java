package com.profi_shop.dto;

import com.profi_shop.model.enums.StockType;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class StockDTO {
    private Long id;
    private Date startDate;
    private Date endDate;
    private StockType type;
    private int discount;

    private List<ProductDTO> participants = new ArrayList<>();
}
