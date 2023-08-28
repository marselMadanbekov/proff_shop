package com.profi_shop.repositories;

import com.profi_shop.model.Notification;
import com.profi_shop.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUser(User user);
    Page<Notification> findByUser(User user,Pageable pageable);
    List<Notification> findByUserAndViewed(User user, Boolean viewed);
    Page<Notification> findByUserAndViewed(User user, Boolean viewed, Pageable pageable);
}
