package com.profi_shop.controllers.adminControllers;

import com.profi_shop.model.User;
import com.profi_shop.services.UserService;
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

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class UsersController {
    private final UserService userService;

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String users(@RequestParam(value = "page", required = false) Optional<Integer> page,
                        @RequestParam(value = "search", required = false) Optional<String> search,
                        @RequestParam(value = "sort",required = false) Optional<Integer> sort,
                        Model model){
        Page<User> users = userService.getUsersFilteredPage(page.orElse(0),search.orElse(null),sort.orElse(0));
        model.addAttribute("users", users);
        model.addAttribute("searchQuery", search.orElse(""));
        model.addAttribute("sort", sort.orElse(0));
        return "admin/users/users";
    }

    @PostMapping("/users/block")
    public ResponseEntity<Map<String,String>> blockUser(@RequestParam Long userId,
                                                        Principal principal){
        Map<String,String> response = new HashMap<>();
        try{
            userService.blockUser(userId, principal.getName());
            response.put("message", "Пользователь успешно заблокирован!");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/users/unlock")
    public ResponseEntity<Map<String,String>> unlockUser(@RequestParam Long userId,
                                                        Principal principal){
        Map<String,String> response = new HashMap<>();
        try{
            userService.unlockUser(userId, principal.getName());
            response.put("message", "Пользователь успешно разблокирован!");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
