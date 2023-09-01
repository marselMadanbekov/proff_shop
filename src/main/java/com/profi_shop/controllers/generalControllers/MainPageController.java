package com.profi_shop.controllers.generalControllers;

import com.profi_shop.model.Category;
import com.profi_shop.model.Store;
import com.profi_shop.services.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping
public class MainPageController {
    private final CartService cartService;
    private final WishlistService wishlistService;
    private final CategoryService categoryService;
    private final MainStoreService mainStoreService;
    private final StoreService storeService;

    @Autowired
    public MainPageController(CartService cartService, WishlistService wishlistService, CategoryService categoryService, MainStoreService mainStoreService, StoreService storeService) {
        this.cartService = cartService;
        this.wishlistService = wishlistService;
        this.categoryService = categoryService;
        this.mainStoreService = mainStoreService;
        this.storeService = storeService;
    }

    @GetMapping("/general-info")
    public ResponseEntity<Map<String, Object>> generalData(HttpServletRequest request,
                                                           Principal principal) {
        Map<String, Object> response = new HashMap<>();
        try {
            String callSupport = mainStoreService.getCallSupportNumber();
            String workTime = mainStoreService.getWorkTime();
            int wishlistCount = 0;
            int cartCount = 0;
            if (principal == null) {
                cartCount = cartService.getCartCount(request);
            } else {
                cartCount = cartService.getCartCount(principal.getName());
                wishlistCount = wishlistService.getWishlistCountByUsername(principal.getName());
            }
            response.put("wishlistCount", wishlistCount);
            response.put("cartCount", cartCount);
            response.put("callSupport", callSupport);
            response.put("workTime", workTime);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/about")
    public String about(Model model){
        List<Store> stores = storeService.getAllStores();
        List<Category> categories = categoryService.getMainCategories();
        model.addAttribute("stores", stores);
        model.addAttribute("categories", categories);
        return "shop/about";
    }
}
