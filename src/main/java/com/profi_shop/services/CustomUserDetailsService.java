package com.profi_shop.services;


import com.profi_shop.exceptions.SearchException;
import com.profi_shop.model.User;
import com.profi_shop.model.enums.Role;
import com.profi_shop.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new SearchException(SearchException.USER_NOT_FOUND));
        return build(user);
    }

    public User loadUserById(Long id){
        User user = userRepository.findUserById(id).orElseThrow(() -> new SearchException(SearchException.USER_NOT_FOUND));
        return build(user);
    }

    public static User build(User user){
        Role role = user.getRole();
        return new User(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.isActive(),
                role
        );
    }
}
