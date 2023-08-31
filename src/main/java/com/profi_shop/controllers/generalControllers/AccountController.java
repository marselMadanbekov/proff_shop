package com.profi_shop.controllers.generalControllers;

import com.profi_shop.auth.requests.SignUpRequest;
import com.profi_shop.model.Category;
import com.profi_shop.model.Coupon;
import com.profi_shop.model.Order;
import com.profi_shop.model.User;
import com.profi_shop.services.CategoryService;
import com.profi_shop.services.CouponService;
import com.profi_shop.services.OrderService;
import com.profi_shop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/account")
public class AccountController {
    private final UserService userService;
    private final CategoryService categoryService;
    private final CouponService couponService;
    private final OrderService orderService;
    @Autowired
    public AccountController(UserService userService, CategoryService categoryService, CouponService couponService, OrderService orderService) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.couponService = couponService;
        this.orderService = orderService;
    }

    @GetMapping("")
    public String myAccount(Principal principal,
                            Model model){
        User user = userService.getUserByUsername(principal.getName());
        List<Category> categories = categoryService.getMainCategories();

        List<Coupon> coupons = couponService.findCouponsByUsername(principal.getName());
        List<Order> recentOrders = orderService.getRecentOrdersByUsername(principal.getName());

        model.addAttribute("orders", recentOrders);
        model.addAttribute("user", user);
        model.addAttribute("coupons", coupons);
        model.addAttribute("categories", categories);
        return "shop/account";
    }

    @PostMapping("/edit")
    public ResponseEntity<Map<String, String>> editUser(SignUpRequest edit,
                                                        Principal principal){
        Map<String, String> response = new HashMap<>();
        try {
            System.out.println(edit.getFirstname());
            userService.editUser(principal,edit);
            response.put("message", "Данные успешно обновлены");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e.getMessage());
            response.put("error", "Ошибка: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
