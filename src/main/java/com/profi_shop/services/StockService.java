package com.profi_shop.services;

import com.profi_shop.exceptions.ExistException;
import com.profi_shop.exceptions.SearchException;
import com.profi_shop.model.Category;
import com.profi_shop.model.Product;
import com.profi_shop.model.Stock;
import com.profi_shop.model.enums.StockType;
import com.profi_shop.model.requests.StockRequest;
import com.profi_shop.repositories.ProductRepository;
import com.profi_shop.repositories.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class StockService {
    private final StockRepository stockRepository;
    private final CategoryService categoryService;

    private final ProductRepository productRepository;

    @Autowired
    public StockService(StockRepository stockRepository, CategoryService categoryService, ProductRepository productRepository) {
        this.stockRepository = stockRepository;
        this.categoryService = categoryService;
        this.productRepository = productRepository;
    }

    public void createStock(StockRequest request) throws ExistException {
        try {
            Stock stock = new Stock();
            stock.setStartDate(request.getStartDate());
            stock.setEndDate(request.getEndDate());
            stock.setDiscount(request.getDiscount());
            stock.setFor_authenticated(request.getAuthenticated());
            stock.setType(request.getType() == 1 ? StockType.CATEGORY : StockType.SELECT);
            if (request.getType() == 1) {
                for (Long categoryId : request.getParticipants().keySet()) {
                    Category current = categoryService.getCategoryById(categoryId);
                    stock.addCategory(current);
                    List<Product> products = productRepository.findAllByCategory(current);
                    stock.addAllParticipants(products);
                }
            } else {
                for (Long productId : request.getParticipants().keySet()) {
                    Product product = productRepository.findById(productId).orElseThrow(() -> new SearchException(SearchException.PRODUCT_NOT_FOUND));
                    stock.addProduct(product);
                }
            }
            stockRepository.save(stock);
        }catch (Exception e){
            throw new ExistException(ExistException.PRODUCT_IS_ALREADY_IN_STOCK);
        }
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

    public Stock getStockById(Long stockId) {
        return stockRepository.findById(stockId).orElseThrow(() -> new SearchException(SearchException.STOCK_NOT_FOUND));
    }

    public Page<Product> getPagedParticipantsByStockId(Long stockId, Integer page){
        Pageable pageable = PageRequest.of(page,15);
        return stockRepository.findParticipantsForStock(stockId, pageable);
    }

    public Page<Stock> getPagedStocks(int page) {
        Pageable pageable = PageRequest.of(page,15);
        return stockRepository.findAll(pageable);
    }

    public Page<Stock> filteredStocksPage(int status, int sort, int page){
        Pageable pageable;
        if(sort == 0) pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.ASC,"discount"));
        else if(sort == 1) pageable = PageRequest.of(page, 10,  Sort.by(Sort.Direction.ASC,"createDate"));
        else if(sort == 2) pageable = PageRequest.of(page, 10,  Sort.by(Sort.Direction.ASC,"startDate"));
        else pageable = PageRequest.of(page, 10,  Sort.by(Sort.Direction.ASC,"endDate"));

        switch (status){
            case 1: return stockRepository.findActiveStocks(LocalDate.now(),pageable);
            case 2: return stockRepository.findInActiveStocks(LocalDate.now(),pageable);
            default: return stockRepository.findAll(pageable);
        }
    }

    public void removeParticipantFromStock(Long stockId, Long productId) {
        Stock stock = getStockById(stockId);
        Product product = getProductById(productId);
        stock.removeParticipant(product);
        stockRepository.save(stock);
    }

    private Product getProductById(Long productId){
        return productRepository.findById(productId).orElseThrow(() -> new SearchException(SearchException.PRODUCT_NOT_FOUND));
    }
}
