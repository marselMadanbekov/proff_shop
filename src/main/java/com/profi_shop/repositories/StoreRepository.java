package com.profi_shop.repositories;

import com.profi_shop.model.Store;
import com.profi_shop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store,Long> {
    Optional<Store> findByAdmin(User admin);
}
