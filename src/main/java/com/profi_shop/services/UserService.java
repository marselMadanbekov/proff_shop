package com.profi_shop.services;

import com.profi_shop.auth.requests.AdminCreateRequest;
import com.profi_shop.auth.requests.SignUpRequest;
import com.profi_shop.exceptions.SearchException;
import com.profi_shop.model.Store;
import com.profi_shop.model.User;
import com.profi_shop.model.enums.Role;
import com.profi_shop.repositories.StoreRepository;
import com.profi_shop.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final StoreRepository storeRepository;
    @Autowired
    public UserService(UserRepository userRepository, StoreRepository storeRepository) {
        this.userRepository = userRepository;
        this.storeRepository = storeRepository;
    }

    public User signUp(SignUpRequest request) {
        User user = new User();
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setRole(Role.CUSTOMER);
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());

        // TODO: добавить шифрование пароля и добавить в поле пользователя
        userRepository.save(user);
        return user;
    }

    public User createAdmin(AdminCreateRequest adminCreate){
        User user = new User();
        user.setFirstname(adminCreate.getFirstname());
        user.setLastname(adminCreate.getLastname());
        user.setRole(Role.ADMIN);
        user.setEmail(adminCreate.getEmail());
        user.setUsername(adminCreate.getUsername());
        user.setPassword(adminCreate.getPassword());
        // TODO: добавить шифрование пароля и добавить в поле пользователя
        Store store = getStoreById(adminCreate.getId());

        store.setAdmin(user);
        storeRepository.save(store);
        return user;
    }


    private Store getStoreById(Long id){
        return storeRepository.findById(id).orElseThrow(() -> new SearchException(SearchException.STORE_NOT_FOUND));
    }
}
