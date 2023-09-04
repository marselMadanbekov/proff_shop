package com.profi_shop.repositories;

import com.profi_shop.model.User;
import com.profi_shop.model.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findUserByUsername(String username);
    Optional<User> findUserById(Long id);
    List<User> findByRole(Role role);
    @Query("SELECT u FROM User u " +
            "WHERE :search IS NULL OR (u.firstname LIKE %:search% OR u.username LIKE %:search% OR u.lastname LIKE %:search%)")
    Page<User> findUserByFirstnameLike(String search, Pageable pageable);


}
