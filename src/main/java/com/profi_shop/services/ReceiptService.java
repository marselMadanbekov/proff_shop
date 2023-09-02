package com.profi_shop.services;

import com.profi_shop.exceptions.SearchException;
import com.profi_shop.model.ProductVariation;
import com.profi_shop.model.Receipt;
import com.profi_shop.model.Store;
import com.profi_shop.model.User;
import com.profi_shop.repositories.ProductVariationRepository;
import com.profi_shop.repositories.ReceiptRepository;
import com.profi_shop.repositories.StoreRepository;
import com.profi_shop.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class ReceiptService {
    private final ReceiptRepository receiptRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final ProductVariationRepository productVariationRepository;

    @Autowired
    public ReceiptService(ReceiptRepository receiptRepository, StoreRepository storeRepository, UserRepository userRepository, ProductVariationRepository productVariationRepository) {
        this.receiptRepository = receiptRepository;
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
        this.productVariationRepository = productVariationRepository;
    }

    public void createReceipt(Long productVariationId, Integer quantity,Long storeId, String actorUsername){
        ProductVariation productVariation = getProductVariationById(productVariationId);
        Store store = getStoreById(storeId);
        User actor = getUserByUsername(actorUsername);
        Receipt receipt = new Receipt();

        receipt.setProductVariation(productVariation);
        receipt.setStore(store);
        receipt.setQuantity(quantity);
        receipt.setActorInfo(actor.getFirstname() + " " + actor.getLastname());

        receiptRepository.save(receipt);
    }

    public List<Receipt> getReceiptsByProductVariationId(Long productVariationId){
        ProductVariation productVariation = getProductVariationById(productVariationId);
        return receiptRepository.findByProductVariation(productVariation);
    }

    public int countOfProductReceipmentsBetween(Long productVariationId, Date startDate, Date endDate){
        ProductVariation productVariation = getProductVariationById(productVariationId);
        return receiptRepository.getTotalQuantityBetweenDates(productVariation,startDate,endDate).orElse(0);
    }

    public List<Receipt> getReceiptsBetween(ProductVariation productVariation, Date startDate, Date endDate){
        return receiptRepository.getReceiptsByProductVariationBetween(productVariation,startDate,endDate);
    }
    private User getUserByUsername(String actorUsername) {
        return userRepository.findUserByUsername(actorUsername).orElseThrow(() -> new SearchException(SearchException.USER_NOT_FOUND));
    }

    private Store getStoreById(Long storeId) {
        return storeRepository.findById(storeId).orElseThrow(() -> new SearchException(SearchException.STORE_NOT_FOUND));
    }

    private ProductVariation getProductVariationById(Long productVariationId){
        return productVariationRepository.findById(productVariationId).orElseThrow(() -> new SearchException(SearchException.PRODUCT_VARIATION_NOT_FOUND));
    }
}
