package com.profi_shop.services;

import com.profi_shop.exceptions.SearchException;
import com.profi_shop.model.ProductVariation;
import com.profi_shop.model.Store;
import com.profi_shop.model.StoreHouse;
import com.profi_shop.repositories.ProductVariationRepository;
import com.profi_shop.repositories.StoreHouseRepository;
import com.profi_shop.repositories.StoreRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreHouseService {
    private final StoreHouseRepository storeHouseRepository;
    private final StoreRepository storeRepository;

    private final ProductVariationRepository productVariationRepository;

    public StoreHouseService(StoreHouseRepository storeHouseRepository, StoreRepository storeRepository, ProductVariationRepository productVariationRepository) {
        this.storeHouseRepository = storeHouseRepository;
        this.storeRepository = storeRepository;
        this.productVariationRepository = productVariationRepository;
    }

    public void createStoreHouseProduct(ProductVariation createdProduct) {
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

        ProductVariation product = getProductVariationById(productId);
        Store store = getStoreById(storeId);
        StoreHouse storeHouse = getStoreHouseByProductAndStore(product,store);
        storeHouse.setQuantity(storeHouse.getQuantity() + quantity);
        storeHouseRepository.save(storeHouse);
    }

    public List<StoreHouse> getStoreHousesByProduct(ProductVariation product){
        return storeHouseRepository.findStoreHouseByProduct(product);
    }

    public StoreHouse getStoreHouseByProductAndStore(ProductVariation product, Store store){
        return storeHouseRepository.findByProductAndStore(product, store).orElseThrow(() -> new SearchException(SearchException.STOCK_NOT_FOUND));
    }
    private Store getStoreById(Long storeId){
        return storeRepository.findById(storeId).orElseThrow(() -> new SearchException(SearchException.STORE_NOT_FOUND));
    }
    private ProductVariation getProductVariationById(Long productId){
        return productVariationRepository.findById(productId).orElseThrow(() -> new SearchException(SearchException.PRODUCT_NOT_FOUND));
    }
}
