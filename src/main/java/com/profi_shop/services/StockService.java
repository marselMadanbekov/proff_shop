package com.profi_shop.services;

import com.profi_shop.exceptions.SearchException;
import com.profi_shop.model.Category;
import com.profi_shop.model.Product;
import com.profi_shop.model.Stock;
import com.profi_shop.model.enums.StockType;
import com.profi_shop.model.requests.StockRequest;
import com.profi_shop.repositories.ProductRepository;
import com.profi_shop.repositories.StockRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class StockService {
    private final StockRepository stockRepository;
    private final CategoryService categoryService;

    private final ProductRepository productRepository;

    public StockService(StockRepository stockRepository, CategoryService categoryService, ProductRepository productRepository) {
        this.stockRepository = stockRepository;
        this.categoryService = categoryService;
        this.productRepository = productRepository;
    }

    public void createStock(StockRequest request){
        Stock stock = new Stock();
        stock.setStartDate(request.getStartDate());
        stock.setEndDate(request.getEndDate());
        stock.setDiscount(request.getDiscount());
        stock.setFor_authenticated(request.getAuthenticated());
        stock.setType(request.getType() == 1 ? StockType.CATEGORY : StockType.SELECT);
        if(request.getType() == 1){
            for(Long categoryId : request.getParticipants().keySet()){
                Category current = categoryService.getCategoryById(categoryId);
                stock.addCategory(current);
                List<Product> products = productRepository.findAllByCategory(current);
                stock.addAllParticipants(products);
            }
        }else{
            for(Long productId: request.getParticipants().keySet()){
                Product product = productRepository.findById(productId).orElseThrow(()-> new SearchException(SearchException.PRODUCT_NOT_FOUND));
                stock.addProduct(product);
            }
        }

        boolean isActive = request.getStartDate().before(Date.valueOf(LocalDate.now())) && request.getEndDate().after(Date.valueOf(LocalDate.now()));
        stock.setActive(isActive);
        stockRepository.save(stock);
    }


    @Scheduled(fixedDelay = 86400000)
    public void updateStockStatus(){
        List<Stock> toDisActivate = stockRepository.findDisActiveStocks(LocalDate.now());
        for(Stock stock : toDisActivate){
            stock.setActive(false);
            stockRepository.save(stock);
        }
        List<Stock> toActivate = stockRepository.findToActiveStocks(LocalDate.now());
        for(Stock stock : toDisActivate){
            stock.setActive(true);
            stockRepository.save(stock);
        }
    }
    public List<Stock> getActiveStocks() {
        return stockRepository.findActiveStocks(LocalDate.now());
    }

    public Stock getStockByProduct(Product product){
        return stockRepository.findByParticipants(product).orElse(null);
    }

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    public Stock getTodayDeals(){
        return stockRepository.findStockWithMaxDiscount().orElse(null);
    }

    public void deleteStockById(Long stockId) {
        Stock stock = getStockById(stockId);
        stockRepository.delete(stock);
    }

    private Stock getStockById(Long stockId) {
        return stockRepository.findById(stockId).orElseThrow(() -> new SearchException(SearchException.STOCK_NOT_FOUND));
    }

    public Page<Stock> getPagedStocks(int page) {
        Pageable pageable = PageRequest.of(page,15);
        return stockRepository.findAll(pageable);
    }
}
