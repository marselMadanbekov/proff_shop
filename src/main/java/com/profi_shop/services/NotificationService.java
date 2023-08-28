package com.profi_shop.services;

import com.profi_shop.exceptions.AccessDeniedException;
import com.profi_shop.exceptions.SearchException;
import com.profi_shop.model.Notification;
import com.profi_shop.model.Store;
import com.profi_shop.model.User;
import com.profi_shop.repositories.NotificationRepository;
import com.profi_shop.services.email.EmailServiceImpl;
import com.profi_shop.settings.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    private final UserService userService;
    private final EmailServiceImpl emailService;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, UserService userService, EmailServiceImpl emailService) {
        this.notificationRepository = notificationRepository;
        this.userService = userService;
        this.emailService = emailService;
    }

    public void createLocalNewOrderNotification(User user, Long order) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setViewed(false);
        notification.setMessage(messageForOrderNotification(order));
        notificationRepository.save(notification);
    }

    public void createLocalOrderRedirectNotification(User targetUser, String sender, Long order) {
        Notification notification = new Notification();
        notification.setUser(targetUser);
        notification.setViewed(false);
        notification.setMessage(messageForOrderRedirecting(order, sender));
        notificationRepository.save(notification);
    }

    public void createLocalNewTransactionNotification(Store targetStore, Store senderStore, Integer amount) {
        Notification notification = new Notification();
        User target;
        if (targetStore == null) {
            target = userService.getSuperAdmin();
            notification.setMessage(messageForTransactionNotification(senderStore.getTown(), "Головной офис", amount));
        } else {
            notification.setMessage(messageForTransactionNotification(senderStore.getTown(), targetStore.getTown(), amount));
            target = targetStore.getAdmin();
        }
        notification.setUser(target);
        notification.setViewed(false);
        notificationRepository.save(notification);
    }

    public Notification getNotificationById(Long id) {
        return notificationRepository.findById(id).orElseThrow(() -> new SearchException(SearchException.NOTIFICATION_NOT_FOUND));
    }

    private String messageForTransactionNotification(String sender, String target, Integer amount) {
        String message = Text.get("MESSAGE_NEW_TRANSACTION");
        message = message.replace("SENDER", sender);
        message = message.replace("TARGET", target);
        return message.replace("AMOUNT", amount.toString());
    }

    private String messageForOrderNotification(Long order) {
        String message = Text.get("MESSAGE_ORDER_NOTIFICATION");
        return message.replace("ORDER_NUMBER", order.toString());
    }

    private String messageForOrderRedirecting(Long order, String sender) {
        String message = Text.get("MESSAGE_REDIRECT_ORDER_NOTIFICATION");
        message = message.replace("ORDER_NUMBER", order.toString());
        return message.replace("SENDER", sender);
    }

    @Scheduled(cron = "0 0/15 0-9 * * *")
    public void emailNotification() {
        List<User> admins = userService.getAdmins();
        for (User admin : admins) {
            List<Notification> notViewedNotifications = notificationRepository.findByUserAndViewed(admin, false);
            if (notViewedNotifications.size() == 0) continue;
            emailService.sendSimpleMessage(admin.getEmail(), "Поступили новые заказы, перейдите на сайт магазина PROFF-SHOP для просмотра");
        }
        System.out.println("Метод с 9-18 сработал! Текущее время: " + System.currentTimeMillis());
    }

    public Boolean checkNotificationsForUser(String name) {
        User user = userService.getUserByUsername(name);
        List<Notification> notifications = notificationRepository.findByUserAndViewed(user,false);
        return notifications.size() > 0;
    }

    public Page<Notification> getPagedNotificationsByUsername(String name, Integer page, Boolean flag) {
        User user = userService.getUserByUsername(name);
        Pageable pageable = PageRequest.of(page, 15);
        if(flag)
            return notificationRepository.findByUserAndViewed(user,false,pageable);
        return notificationRepository.findByUser(user,pageable);
    }

    public void notificationViewed(Long notificationId, String name) {
        Notification notification = getNotificationById(notificationId);
        User user = userService.getUserByUsername(name);

        if(!notification.getUser().equals(user))
            throw new AccessDeniedException(AccessDeniedException.FOREIGN_ACCOUNT);

        notification.setViewed(true);
        notificationRepository.save(notification);
    }
}
