package com.profi_shop.services;

import com.profi_shop.exceptions.InvalidDataException;
import com.profi_shop.model.MainStore;
import com.profi_shop.repositories.MainStoreRepository;
import com.profi_shop.validations.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MainStoreService {
    private final MainStoreRepository mainStoreRepository;

    @Autowired
    public MainStoreService(MainStoreRepository mainStoreRepository) {
        this.mainStoreRepository = mainStoreRepository;
    }

    public MainStore getMainStore(){
        Optional<MainStore> mainStore = mainStoreRepository.findFirstById(1L);
        return mainStore.orElseGet(() -> {
            MainStore newMainStore = new MainStore();
            return mainStoreRepository.save(newMainStore);
        });
    }

    public void addNewPhoneNumber(String newNumber) throws InvalidDataException {
        MainStore mainStore = getMainStore();
        mainStore.addPhoneNumber(Validator.validNumber(newNumber));
        mainStoreRepository.save(mainStore);
    }

    public void setEmail(String email){
        MainStore mainStore = getMainStore();
        mainStore.setEmail(email);
        mainStoreRepository.save(mainStore);
    }

    public void setAddress(String address){
        MainStore mainStore = getMainStore();
        mainStore.setAddress(address);
        mainStoreRepository.save(mainStore);
    }

    public void setTown(String town){
        MainStore mainStore = getMainStore();
        mainStore.setTown(town);
        mainStoreRepository.save(mainStore);
    }

    public void setState(String state){
        MainStore mainStore = getMainStore();
        mainStore.setState(state);
        mainStoreRepository.save(mainStore);
    }

    public void setWorkingTime(String time){
        MainStore mainStore = getMainStore();
        mainStore.setWorking_time(time);
        mainStoreRepository.save(mainStore);
    }

    public void setInstagram(String instagram) {
        MainStore mainStore = getMainStore();
        mainStore.setInstagram(instagram);
        mainStoreRepository.save(mainStore);
    }

    public void deletePhone(String phone) {
        MainStore mainStore = getMainStore();
        mainStore.deletePhone(phone);
        mainStoreRepository.save(mainStore);
    }
}
