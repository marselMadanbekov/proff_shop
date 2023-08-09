package com.profi_shop.controllers.generalControllers;

import com.profi_shop.model.Wishlist;
import com.profi_shop.services.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    @Autowired
    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping("")
    public String wishlist(Principal principal,
                           Model model){
        Wishlist wishlist = wishlistService.getByUsername(principal.getName());
        model.addAttribute("wishlist",wishlist);
        return "shop/wishlist";
    }

    @GetMapping("/addToWishlist")
    public ResponseEntity<Map<String,String>> addToWishlist(@RequestParam("productId") Long productId,
                                                            Principal principal){
        Map<String,String> response = new HashMap<>();
        if(principal == null){
            response.put("error", "Войдите чтобы добавить товар в избранное");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        try{
            System.out.println(principal.getName());
            System.out.println(productId);
            wishlistService.addProductToWishlist(productId,principal);
            response.put("message", "Продукт успешно добавлен в избранное");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e.getMessage());
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/remove")
    public ResponseEntity<Map<String,String>> removeFromWishlist(@RequestParam("productId") Long productId,
                                                                 Principal principal){
        Map<String,String> response = new HashMap<>();
        if(principal == null){
            response.put("error", "Ошибка: Не удалось распознать пользователя. Войдите");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        try {
            wishlistService.removeProduct(productId,principal);
            response.put("message","Продукт удален из избранных");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
