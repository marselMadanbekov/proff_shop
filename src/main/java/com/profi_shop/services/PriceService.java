package com.profi_shop.services;

import com.profi_shop.model.Product;
import com.profi_shop.model.Stock;
import com.profi_shop.repositories.CouponRepository;
import com.profi_shop.repositories.ProductRepository;
import com.profi_shop.repositories.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;

@Service
public class PriceService {
    private final StockRepository stockRepository;
    private final CouponRepository couponRepository;

    @Autowired
    public PriceService(StockRepository stockRepository, CouponRepository couponRepository) {
        this.stockRepository = stockRepository;
        this.couponRepository = couponRepository;
    }

    public Integer priceOfProductForUser(Product product, boolean authenticated){
        Stock stock = stockRepository.findByParticipants(product).orElse(null);
        if (stock != null){
            boolean isActive = stock.getStartDate().before(Date.valueOf(LocalDate.now())) && stock.getEndDate().after(Date.valueOf(LocalDate.now()));
            if(!isActive) return product.getPrice();
            if(authenticated)
                return (int) (product.getPrice() - (product.getPrice() * stock.getDiscount()) / 100.0);
            if(stock.isFor_authenticated())
                return product.getPrice();
            return (int) (product.getPrice() - (product.getPrice() * stock.getDiscount()) / 100.0);
        }
        return product.getPrice();
    }

    public Integer getDiscountForProductForUser(Product product, boolean authenticated){
        Stock stock = stockRepository.findByParticipants(product).orElse(null);
        if (stock != null){
            boolean isActive = stock.getStartDate().before(Date.valueOf(LocalDate.now())) && stock.getEndDate().after(Date.valueOf(LocalDate.now()));
            if(!isActive) return 0;
            if(authenticated)
                return stock.getDiscount();
            if(stock.isFor_authenticated())
                return 0;
            return stock.getDiscount();
        }
        return 0;
    }
}
