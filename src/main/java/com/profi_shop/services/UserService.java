package com.profi_shop.services;

import com.profi_shop.auth.requests.AdminCreateRequest;
import com.profi_shop.auth.requests.SignUpRequest;
import com.profi_shop.exceptions.ExistException;
import com.profi_shop.exceptions.SearchException;
import com.profi_shop.model.Store;
import com.profi_shop.model.User;
import com.profi_shop.model.enums.Role;
import com.profi_shop.repositories.StoreRepository;
import com.profi_shop.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final StoreRepository storeRepository;

    private final PasswordEncoder bCryptPasswordEncoder;
    @Autowired
    public UserService(UserRepository userRepository, StoreRepository storeRepository, PasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.storeRepository = storeRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User signUp(SignUpRequest request) {
        User user = new User();
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setRole(Role.ROLE_CUSTOMER);
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        user.setActive(true);

        userRepository.save(user);
        return user;
    }

    public void createAdmin(AdminCreateRequest adminCreate) {
        try {
            User user = new User();
            user.setFirstname(adminCreate.getFirstname());
            user.setLastname(adminCreate.getLastname());
            user.setRole(Role.ROLE_ADMIN);
            user.setEmail(adminCreate.getEmail());
            user.setUsername(adminCreate.getUsername());
            user.setPassword(bCryptPasswordEncoder.encode(adminCreate.getPassword()));
            user.setActive(true);
            Store store = getStoreById(adminCreate.getStoreId());

            userRepository.save(user);
            store.setAdmin(user);
            storeRepository.save(store);
        }catch (Exception e){
            throw new ExistException(ExistException.USER_EXISTS);
        }
    }


    private Store getStoreById(Long id){
        return storeRepository.findById(id).orElseThrow(() -> new SearchException(SearchException.STORE_NOT_FOUND));
    }

    public User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username).orElseThrow(() -> new SearchException(SearchException.USER_NOT_FOUND));
    }

    public Page<User> getUsersFilteredPage(Integer page, String search, Integer sort) {
        Pageable pageable = null;
        if(sort != 0){
            if(sort == 1)   pageable = PageRequest.of(page,9, Sort.by(Sort.Direction.DESC,"create_date"));
            else   pageable = PageRequest.of(page,9, Sort.by(Sort.Direction.ASC,"name"));
        }
        else{
            pageable = PageRequest.of(page,9);
        }

        if(search != null && !search.equals("")){
            return userRepository.findUserByFirstname(search,pageable);
        }
        return userRepository.findAll(pageable);
    }
}
