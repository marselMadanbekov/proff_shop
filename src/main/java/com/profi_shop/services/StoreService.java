package com.profi_shop.services;

import com.profi_shop.dto.StoreDTO;
import com.profi_shop.model.Store;
import com.profi_shop.repositories.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreService {
    private final StoreRepository storeRepository;

    @Autowired
    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public Store createStore(StoreDTO storeToCreate){
        Store store = new Store();
        store.setName(storeToCreate.getName());
        store.setTown(storeToCreate.getTown());
        store.setPhone_number(storeToCreate.getPhone_number());
        return storeRepository.save(store);
    }

    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    public Page<Store> getPagedStores(int page, int size){
        Pageable pageable = PageRequest.of(page,size);
        return storeRepository.findAll(pageable);
    }

}
