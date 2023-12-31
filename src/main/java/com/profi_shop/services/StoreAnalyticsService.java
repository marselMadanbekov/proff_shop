package com.profi_shop.services;

import com.profi_shop.dto.ProductQuantityDTO;
import com.profi_shop.dto.StoreAnalyticsDTO;
import com.profi_shop.exceptions.SearchException;
import com.profi_shop.model.Order;
import com.profi_shop.model.ProductVariation;
import com.profi_shop.model.Store;
import com.profi_shop.model.enums.OrderStatus;
import com.profi_shop.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
public class StoreAnalyticsService {

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;
    private final TransactionRepository transactionRepository;
    private final ConsumptionRepository consumptionRepository;
    private final StoreHouseRepository storeHouseRepository;
    private final StoreRepository storeRepository;

    @Autowired
    public StoreAnalyticsService(OrderRepository orderRepository, ProductRepository productRepository, TransactionRepository transactionRepository, ConsumptionRepository consumptionRepository, StoreHouseRepository storeHouseRepository, StoreRepository storeRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.transactionRepository = transactionRepository;
        this.consumptionRepository = consumptionRepository;
        this.storeHouseRepository = storeHouseRepository;
        this.storeRepository = storeRepository;
    }

    public StoreAnalyticsDTO getStoreAnalytics(Long storeId, Date startDate, Date endDate) {
        Store store = getStoreById(storeId);
        StoreAnalyticsDTO analyticsDTO = new StoreAnalyticsDTO();
        analyticsDTO.setOnlineSales(sumOfOnlineSalesByStore(store, startDate, endDate));
        analyticsDTO.setOfflineSales(sumOfOfflineSalesByStore(store, startDate, endDate));
        analyticsDTO.setCanceledOrders(countOfCanceledOrdersByStore(store, startDate, endDate));
        analyticsDTO.setConsumptionsAmount(consumptionsAmount(store,startDate,endDate));
        analyticsDTO.setTransactionsAmount(transactionsAmount(store,startDate,endDate));
        return analyticsDTO;
    }

    public int sumOfOnlineSalesByStore(Store store, Date startDate, Date endDate) {
        int sum = orderRepository.calculateTotalOnlineRevenueForStoreLessShipment(OrderStatus.FINISHED, store, startDate, endDate).orElse(0);
        sum += orderRepository.calculateTotalOnlineRevenueForStoreMinusShipment(OrderStatus.FINISHED, store, startDate, endDate).orElse(0);
        return sum;
    }

    public int sumOfOfflineSalesByStore(Store store, Date startDate, Date endDate) {
        return orderRepository.calculateTotalOfflineRevenueForStore(store, startDate, endDate, OrderStatus.FINISHED).orElse(0);
    }

    public int countOfCanceledOrdersByStore(Store store, Date startDate, Date endDate) {
        return orderRepository.countOrdersByStatus(OrderStatus.CANCELED, store, startDate, endDate).orElse(0);
    }

    public int transactionsAmount(Store store, Date startDate, Date endDate) {
        return transactionRepository.getTotalTransactionAmountForStore(store,startDate,endDate).orElse(0);
    }

    public int consumptionsAmount(Store store, Date startDate, Date endDate) {
        return consumptionRepository.getTotalConsumptionAmountForStore(store,startDate,endDate).orElse(0);
    }

    private Store getStoreById(Long storeId) {
        return storeRepository.findById(storeId).orElseThrow(() -> new SearchException(SearchException.STORE_NOT_FOUND));
    }

    public Page<Order> getSalesByStore(Long storeId, Integer page, Boolean isOnline) {
        Store store = getStoreById(storeId);
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.ASC, "date"));

        if (isOnline) {
            return orderRepository.getPageOnlineSales(store, pageable, OrderStatus.FINISHED);
        } else {
            return orderRepository.getPageOfflineSales(store, pageable, OrderStatus.FINISHED);
        }
    }

    public Page<Object[]> findProductsAndQuantitiesByStore(Store store, Pageable pageable) {
        return storeHouseRepository.findProductsAndQuantitiesByStore(store, pageable);
    }

    public Page<ProductQuantityDTO> getProductAndQuantityPage(Long storeId, Integer page, Integer sort) {

        Store store = getStoreById(storeId);
        Pageable pageable;
        if (sort == 0)
            pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.ASC, "quantity"));
        else pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "quantity"));
        Page<Object[]> res = findProductsAndQuantitiesByStore(store, pageable);
        return res.map(array -> {
            ProductVariation productVariation = (ProductVariation) array[0];
            Integer quantity = (Integer) array[1];
            return new ProductQuantityDTO(
                    productVariation.getParent().getId(),
                    productVariation.getParent().getPhotos().get(0),
                    productVariation.getParent().getName(),
                    productVariation.getParent().getPrice(),
                    productVariation.getSize(),
                    quantity
            );
        });
    }

}
