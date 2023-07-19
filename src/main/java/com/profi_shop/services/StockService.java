package com.profi_shop.services;

import com.profi_shop.exceptions.SearchException;
import com.profi_shop.model.Product;
import com.profi_shop.model.StoreHouse;
import com.profi_shop.model.Store;
import com.profi_shop.repositories.ProductRepository;
import com.profi_shop.repositories.StockRepository;
import com.profi_shop.repositories.StoreRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {
    private final StockRepository stockRepository;
    private final StoreRepository storeRepository;

    private final ProductRepository productRepository;

    public StockService(StockRepository stockRepository, StoreRepository storeRepository, ProductRepository productRepository) {
        this.stockRepository = stockRepository;
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
    }

    public void createStocksProduct(Product createdProduct) {
        List<Store> stores = storeRepository.findAll();
        for(Store store: stores){
            StoreHouse storeHouse = new StoreHouse();
            storeHouse.setProduct(createdProduct);
            storeHouse.setStore(store);
            storeHouse.setQuantity(0);
            stockRepository.save(storeHouse);
        }
    }

    public void quantityUp(Long storeId, Long productId, int quantity) {
        Product product = getProductById(productId);
        Store store = getStoreById(storeId);
        StoreHouse storeHouse = getStockByProductAndStore(product,store);
        storeHouse.setQuantity(storeHouse.getQuantity() + quantity);

        stockRepository.save(storeHouse);
    }

    public StoreHouse getStockByProductAndStore(Product product, Store store){
        return stockRepository.findByProductAndStore(product, store).orElseThrow(() -> new SearchException(SearchException.STOCK_NOT_FOUND));
    }
    private Store getStoreById(Long storeId){
        return storeRepository.findById(storeId).orElseThrow(() -> new SearchException(SearchException.STORE_NOT_FOUND));
    }
    private Product getProductById(Long productId){
        return productRepository.findById(productId).orElseThrow(() -> new SearchException(SearchException.PRODUCT_NOT_FOUND));
    }
}
