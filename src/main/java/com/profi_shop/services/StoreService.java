package com.profi_shop.services;

import com.profi_shop.dto.StoreDTO;
import com.profi_shop.exceptions.InvalidDataException;
import com.profi_shop.exceptions.SearchException;
import com.profi_shop.model.Store;
import com.profi_shop.repositories.StoreRepository;
import com.profi_shop.validations.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreService {
    private final StoreRepository storeRepository;

    private final StoreHouseService storeHouseService;
    @Autowired
    public StoreService(StoreRepository storeRepository, StoreHouseService storeHouseService) {
        this.storeRepository = storeRepository;
        this.storeHouseService = storeHouseService;
    }

    public void createStore(StoreDTO storeToCreate) throws InvalidDataException {
        Store store = new Store();
        store.setName(storeToCreate.getName());
        store.setTown(storeToCreate.getTown());
        store.setPhone_number(Validator.validNumber(storeToCreate.getPhone_number()));
        store.setAddress(storeToCreate.getAddress());
        storeRepository.save(store);
    }

    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    public Page<Store> getPagedStores(int page, int size){
        Pageable pageable = PageRequest.of(page,size);
        return storeRepository.findAll(pageable);
    }

    public Store getStoreById(Long storeId) {
        return storeRepository.findById(storeId).orElseThrow(() -> new SearchException(SearchException.STORE_NOT_FOUND));
    }
}
