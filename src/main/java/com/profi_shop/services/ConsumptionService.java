package com.profi_shop.services;

import com.profi_shop.exceptions.AccessDeniedException;
import com.profi_shop.exceptions.SearchException;
import com.profi_shop.model.Consumption;
import com.profi_shop.model.Store;
import com.profi_shop.model.User;
import com.profi_shop.repositories.ConsumptionRepository;
import com.profi_shop.repositories.StoreRepository;
import com.profi_shop.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ConsumptionService {
    private final ConsumptionRepository consumptionRepository;
    private final UserRepository userRepository;

    private final StoreRepository storeRepository;

    @Autowired
    public ConsumptionService(ConsumptionRepository consumptionRepository, UserRepository userRepository, StoreRepository storeRepository) {
        this.consumptionRepository = consumptionRepository;
        this.userRepository = userRepository;
        this.storeRepository = storeRepository;
    }

    public void createConsumptionByStore(Long storeId, Integer amount, String description, String username) {
        Store store = getStoreById(storeId);
        User creator = getUserByUsername(username);

        if(!creator.equals(store.getAdmin()))
            throw new AccessDeniedException(AccessDeniedException.FOREIGN_BRANCH);

        Consumption consumption = new Consumption();
        consumption.setAmount(amount);
        consumption.setDescription(description);
        consumption.setStore(store);
        consumptionRepository.save(consumption);
        store.balanceDown(consumption.getAmount());
        storeRepository.save(store);
    }

    private Store getStoreById(Long storeId) {
        return storeRepository.findById(storeId).orElseThrow(() -> new SearchException(SearchException.STORE_NOT_FOUND));
    }

    private User getUserByUsername(String username){
        return userRepository.findUserByUsername(username).orElseThrow(() -> new SearchException(SearchException.USER_NOT_FOUND));
    }

    public Page<Consumption> getPagedConsumptions(Long storeId, Integer page, Integer sort) {
        Store store = getStoreById(storeId);
        Pageable pageable = switch (sort) {
            case 0 -> PageRequest.of(page, 10, Sort.by(Sort.Direction.ASC, "date"));
            case 1 -> PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "date"));
            case 2 -> PageRequest.of(page, 10, Sort.by(Sort.Direction.ASC, "amount"));
            default -> PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "amount"));
        };
        return consumptionRepository.findByStore(store, pageable);
    }
}
