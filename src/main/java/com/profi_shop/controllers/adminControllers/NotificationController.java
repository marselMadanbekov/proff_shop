package com.profi_shop.controllers.adminControllers;

import com.profi_shop.model.Notification;
import com.profi_shop.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.swing.text.html.Option;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class NotificationController {
    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/notifications")
    public String notifications(@RequestParam(value = "page",required = false) Optional<Integer> page,
                                @RequestParam(value = "flag",required = false) Optional<Boolean> onlyNotViewed,
                                Principal principal, Model model){
        Page<Notification> notifications = notificationService.getPagedNotificationsByUsername(principal.getName(), page.orElse(0), onlyNotViewed.orElse(false));
        model.addAttribute("notifications",notifications);
        model.addAttribute("username", principal.getName());
        return "admin/notifications/notifications";
    }

    @GetMapping("/check-notifications")
    public ResponseEntity<Map<String,Boolean>> checkNotifications(Principal principal){
        Map<String,Boolean> response = new HashMap<>();
        try{
            Boolean has = notificationService.checkNotificationsForUser(principal.getName());
            response.put("hasNotifications", has);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            response.put("error", true);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/notification-viewed")
    public ResponseEntity<Map<String,String>> notificationViewed(@RequestParam Long notificationId,
                                                                 Principal principal) {
        Map<String, String> response = new HashMap<>();
        try {
            notificationService.notificationViewed(notificationId, principal.getName());
            response.put("message", "Успешно");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
