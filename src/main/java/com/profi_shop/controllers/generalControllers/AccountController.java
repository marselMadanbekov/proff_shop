package com.profi_shop.controllers.generalControllers;

import com.profi_shop.model.User;
import com.profi_shop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/account")
public class AccountController {
    private final UserService userService;

    @Autowired
    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String myAccount(Principal principal,
                            Model model){
        User user = userService.getUserByUsername(principal.getName());
        model.addAttribute("user", user);
        return "shop/account";
    }
}
