package com.profi_shop.services;

import com.profi_shop.exceptions.SearchException;
import com.profi_shop.model.Category;
import com.profi_shop.model.Product;
import com.profi_shop.model.Stock;
import com.profi_shop.model.enums.StockType;
import com.profi_shop.model.requests.StockRequest;
import com.profi_shop.repositories.ProductRepository;
import com.profi_shop.repositories.StockRepository;
import org.springframework.stereotype.Service;

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
        stockRepository.save(stock);
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
}
