package com.profi_shop.repositories;

import com.profi_shop.model.MainStore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MainStoreRepository extends JpaRepository<MainStore, Long> {
    Optional<MainStore> findFirstById(Long id);
}
