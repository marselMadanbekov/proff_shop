package com.profi_shop.services;

import com.profi_shop.exceptions.SearchException;
import com.profi_shop.model.Notification;
import com.profi_shop.model.User;
import com.profi_shop.repositories.NotificationRepository;
import com.profi_shop.services.email.EmailServiceImpl;
import com.profi_shop.settings.Text;
import org.springframework.beans.factory.annotation.Autowired;
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

    public void createLocalNewOrderNotification(User user, Long order){
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setViewed(false);
        notification.setMessage(messageForOrderNotification(order));
        notificationRepository.save(notification);
    }

    public void createLocalOrderRedirectNotification(User targetUser, String sender, Long order){
        Notification notification = new Notification();
        notification.setUser(targetUser);
        notification.setViewed(false);
        notification.setMessage(messageForOrderRedirecting(order,sender));
        notificationRepository.save(notification);
    }
    public Notification getNotificationById(Long id){
        return notificationRepository.findById(id).orElseThrow(() -> new SearchException(SearchException.NOTIFICATION_NOT_FOUND));
    }

    private String messageForOrderNotification(Long order){
        String message = Text.get("MESSAGE_ORDER_NOTIFICATION");
        return message.replace("ORDER_NUMBER", order.toString());
    }
    private String messageForOrderRedirecting(Long order, String sender){
        String message = Text.get("MESSAGE_REDIRECT_ORDER_NOTIFICATION");
        message = message.replace("ORDER_NUMBER", order.toString());
        return message.replace("SENDER", sender);
    }

    @Scheduled(cron = "0 0/15 0-9 * * *")
    public void emailNotification() {
        List<User> admins = userService.getAdmins();
        for(User admin : admins){
            List<Notification> notViewedNotifications = notificationRepository.findByUserAndViewed(admin, false);
            if(notViewedNotifications.size() == 0) continue;
            emailService.sendSimpleMessage(admin.getEmail(), "Поступили новые заказы, перейдите на сайт магазина PROFF-SHOP для просмотра");
        }
        System.out.println("Метод с 9-18 сработал! Текущее время: " + System.currentTimeMillis());
    }
}
