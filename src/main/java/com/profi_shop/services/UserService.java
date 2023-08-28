package com.profi_shop.services;

import com.profi_shop.auth.requests.AdminCreateRequest;
import com.profi_shop.auth.requests.SignUpRequest;
import com.profi_shop.exceptions.ExistException;
import com.profi_shop.exceptions.InvalidDataException;
import com.profi_shop.exceptions.SearchException;
import com.profi_shop.model.*;
import com.profi_shop.model.enums.Role;
import com.profi_shop.repositories.*;
import com.profi_shop.services.email.EmailServiceImpl;
import com.profi_shop.validations.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Random;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final WishlistRepository wishlistRepository;
    private final CartRepository cartRepository;
    private final StoreRepository storeRepository;
    private final EmailServiceImpl emailService;
    private final PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, WishlistRepository wishlistRepository, CartRepository cartRepository, StoreRepository storeRepository, EmailServiceImpl emailService, PasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.wishlistRepository = wishlistRepository;
        this.cartRepository = cartRepository;
        this.storeRepository = storeRepository;
        this.emailService = emailService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User signUp(SignUpRequest request) throws InvalidDataException, ExistException {
        User user = new User();
        user.setFirstname(Validator.validFirstname(request.getFirstname()));
        user.setLastname(Validator.validLastname(request.getLastname()));
        user.setPhone_number(Validator.validNumber(request.getPhone_number()));
        user.setRole(Role.ROLE_CUSTOMER);
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        user.setActive(true);
        try {
            userRepository.save(user);
            Wishlist wishlist = new Wishlist();
            wishlist.setCustomer(user);
            wishlistRepository.save(wishlist);

        }catch (Exception e){
            throw new ExistException(ExistException.USER_EXISTS);
        }
        cartRepository.save(new Cart(user));
        return user;
    }

    public void createAdmin(AdminCreateRequest adminCreate) throws ExistException {
        try {
            User user = new User();
            user.setFirstname(Validator.validFirstname(adminCreate.getFirstname()));
            user.setLastname(Validator.validLastname(adminCreate.getLastname()));
            user.setPhone_number(Validator.validNumber(adminCreate.getPhone_number()));
            user.setRole(Role.ROLE_ADMIN);
            user.setEmail(adminCreate.getEmail());
            user.setUsername(adminCreate.getUsername());
            user.setPassword(bCryptPasswordEncoder.encode(adminCreate.getPassword()));
            user.setActive(true);
            Store store = getStoreById(adminCreate.getStoreId());
            userRepository.save(user);

            if(store.getAdmin() != null){
                userRepository.delete(store.getAdmin());
            }
            store.setAdmin(user);
            storeRepository.save(store);

            wishlistRepository.save(new Wishlist(user));

            cartRepository.save(new Cart(user));
        } catch (Exception e) {
            throw new ExistException(ExistException.USER_EXISTS);
        }
    }


    private Store getStoreById(Long id) {
        return storeRepository.findById(id).orElseThrow(() -> new SearchException(SearchException.STORE_NOT_FOUND));
    }

    public User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username).orElseThrow(() -> new SearchException(SearchException.USER_NOT_FOUND));
    }

    public Page<User> getUsersFilteredPage(Integer page, String search, Integer sort) {
        Pageable pageable = null;
        if (sort != 0) {
            if (sort == 1) pageable = PageRequest.of(page, 9, Sort.by(Sort.Direction.DESC, "create_date"));
            else pageable = PageRequest.of(page, 9, Sort.by(Sort.Direction.ASC, "name"));
        } else {
            pageable = PageRequest.of(page, 9);
        }

        if (search != null && !search.equals("")) {
            return userRepository.findUserByFirstname(search, pageable);
        }
        return userRepository.findAll(pageable);
    }

    public void resetPassword(String username) {
        User user = getUserByUsername(username);
        String password = generatePassword(user);

        emailService.sendPasswordResetMail(user.getEmail(), password);

        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
    }

    private String generatePassword(User user) {
        int length = 10; // Длина нового пароля
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()"; // Допустимые символы для пароля
        StringBuilder password = new StringBuilder();

        Random random = new Random();

        // Генерируем случайные символы для создания пароля
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            password.append(characters.charAt(index));
        }

        return password.toString();
    }

    public void editUser(Principal principal, SignUpRequest edit) {
        User user = getUserByUsername(principal.getName());
        try {
            if (edit.getEmail() != null && !edit.getEmail().isEmpty()) user.setEmail(edit.getEmail());
            if (edit.getFirstname() != null && !edit.getFirstname().isEmpty())
                user.setFirstname(Validator.validFirstname(edit.getFirstname()));
            if (edit.getLastname() != null && !edit.getLastname().isEmpty())
                user.setLastname(Validator.validLastname(edit.getLastname()));
            if (edit.getPhone_number() != null && !edit.getPhone_number().isEmpty())
                user.setPhone_number(Validator.validNumber(edit.getPhone_number()));
            if (edit.getPassword() != null && !edit.getPassword().isEmpty())
                user.setPassword(bCryptPasswordEncoder.encode(edit.getPassword()));

            userRepository.save(user);
        } catch (InvalidDataException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<User> getAdmins() {
        return userRepository.findByRole(Role.ROLE_ADMIN);
    }

    public User getSuperAdmin() {
        return userRepository.findByRole(Role.ROLE_SUPER_ADMIN).get(0);
    }
}
