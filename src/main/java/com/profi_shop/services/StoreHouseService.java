package com.profi_shop.services;

import com.profi_shop.exceptions.SearchException;
import com.profi_shop.model.Product;
import com.profi_shop.model.StoreHouse;
import com.profi_shop.model.Store;
import com.profi_shop.repositories.ProductRepository;
import com.profi_shop.repositories.StoreHouseRepository;
import com.profi_shop.repositories.StoreRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreHouseService {
    private final StoreHouseRepository storeHouseRepository;
    private final StoreRepository storeRepository;

    private final ProductRepository productRepository;

    public StoreHouseService(StoreHouseRepository storeHouseRepository, StoreRepository storeRepository, ProductRepository productRepository) {
        this.storeHouseRepository = storeHouseRepository;
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
            storeHouseRepository.save(storeHouse);
        }
    }

    public void quantityUp(Long storeId, Long productId, int quantity) {
        Product product = getProductById(productId);
        Store store = getStoreById(storeId);
        StoreHouse storeHouse = getStoreHouseByProductAndStore(product,store);
        storeHouse.setQuantity(storeHouse.getQuantity() + quantity);

        storeHouseRepository.save(storeHouse);
    }

    public List<StoreHouse> getStoreHousesByProduct(Product product){
        return storeHouseRepository.findStoreHouseByProduct(product);
    }

    public StoreHouse getStoreHouseByProductAndStore(Product product, Store store){
        return storeHouseRepository.findByProductAndStore(product, store).orElseThrow(() -> new SearchException(SearchException.STOCK_NOT_FOUND));
    }
    private Store getStoreById(Long storeId){
        return storeRepository.findById(storeId).orElseThrow(() -> new SearchException(SearchException.STORE_NOT_FOUND));
    }
    private Product getProductById(Long productId){
        return productRepository.findById(productId).orElseThrow(() -> new SearchException(SearchException.PRODUCT_NOT_FOUND));
    }
}
