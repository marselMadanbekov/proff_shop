package com.profi_shop.repositories;

import com.profi_shop.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    Page<Category> findByParentIsNull(Pageable pageable);
    List<Category> findByParentIsNull();

    Optional<Category> findById(Long id);
}
