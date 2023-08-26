package com.profi_shop.services;

import com.profi_shop.exceptions.SearchException;
import com.profi_shop.model.Store;
import com.profi_shop.model.enums.OrderStatus;
import com.profi_shop.repositories.OrderRepository;
import com.profi_shop.repositories.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
public class StoreAnalyticsService {

    private final OrderRepository orderRepository;

    private final StoreRepository storeRepository;

    @Autowired
    public StoreAnalyticsService(OrderRepository orderRepository, StoreRepository storeRepository) {
        this.orderRepository = orderRepository;
        this.storeRepository = storeRepository;
    }

    public int sumOfOnlineSalesByStore(Long storeId, Date startDate, Date endDate){
        Store store = getStoreById(storeId);
        return orderRepository.calculateTotalOnlineRevenueForStore(OrderStatus.FINISHED,store,startDate, endDate).orElse(0);
    }

    public int sumOfOfflineSalesByStore(Long storeId, Date startDate, Date endDate){
        Store store = getStoreById(storeId);
        return orderRepository.calculateTotalOfflineRevenueForStore(store,startDate,endDate,OrderStatus.FINISHED).orElse(0);
    }

    public int countOfCanceledOrdersByStore(Long storeId, Date startDate, Date endDate){
        Store store = getStoreById(storeId);
        return orderRepository.countOrdersByStatus(OrderStatus.CANCELED, store, startDate, endDate).orElse(0);
    }
    private Store getStoreById(Long storeId) {
        return storeRepository.findById(storeId).orElseThrow(() -> new SearchException(SearchException.STORE_NOT_FOUND));
    }


}
