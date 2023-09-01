package com.profi_shop.repositories;

import com.profi_shop.model.MainPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MainPageRepository extends JpaRepository<MainPage, Long> {
    Optional<MainPage> findFirstById(long l);
}
