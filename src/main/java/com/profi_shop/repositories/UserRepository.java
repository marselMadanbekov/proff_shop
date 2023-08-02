package com.profi_shop.repositories;

import com.profi_shop.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findUserByUsername(String username);
    Optional<User> findUserById(Long id);

    Page<User> findUserByFirstname(String search, Pageable pageable);
}
