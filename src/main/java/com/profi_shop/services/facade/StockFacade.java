package com.profi_shop.services.facade;

import com.profi_shop.dto.StockDTO;
import com.profi_shop.model.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockFacade {
    private final ProductFacade productFacade;

    @Autowired
    public StockFacade(ProductFacade productFacade) {
        this.productFacade = productFacade;
    }

    public StockDTO stockToStockDTO(Stock stock){
        StockDTO stockDTO = new StockDTO();
        stockDTO.setId(stock.getId());
        stockDTO.setType(stock.getType());
        stockDTO.setEndDate(stock.getEndDate());
        stockDTO.setStartDate(stock.getStartDate());
        stockDTO.setParticipants(productFacade.mapToProductDTOList(stock.getParticipants()));
        stockDTO.setDiscount(stock.getDiscount());
        return stockDTO;
    }
}
