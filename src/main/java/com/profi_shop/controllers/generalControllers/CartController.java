package com.profi_shop.controllers.generalControllers;

import com.profi_shop.model.Cart;
import com.profi_shop.model.requests.CartUpdateRequest;
import com.profi_shop.services.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("")
    public String cart(Principal principal,
                       HttpServletRequest request,
                       HttpServletResponse response,
                       Model model) {
        Cart cart;
        if (principal == null) {
            cart = cartService.getCartByRequestCookies(request);
            cartService.saveCartToCookie(cart, response);
        } else {
            cart = cartService.getCartByUsername(principal.getName());
        }
        model.addAttribute("cart", cart);
        return "shop/cart";
    }

    @GetMapping("/add")
    public ResponseEntity<Map<String, String>> addToCart(@RequestParam("productId") Long productId,
                                                         HttpServletRequest request,
                                                         HttpServletResponse response,
                                                         Principal principal) {
        Map<String, String> responseMessage = new HashMap<>();
        Cart cart;

        System.out.println("productId to add -> " + productId);
        try {
            if (principal == null) {
                cart = cartService.addProductToCart(request, productId);
                cartService.saveCartToCookie(cart, response);
                responseMessage.put("message", "Продукт успешно добавлен в корзину");
            } else {
                cart = cartService.addProductToCart(principal.getName(), productId);
                responseMessage.put("message", "Продукт успешно добавлен в корзину");
            }
        } catch (Exception e) {
            responseMessage.put("error", e.getMessage());
            return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @GetMapping("/remove")
    public ResponseEntity<Map<String, String>> removeFromCart(@RequestParam("productId") Long productId,
                                                              HttpServletRequest request,
                                                              HttpServletResponse response,
                                                              Principal principal) {
        Map<String, String> responseMessage = new HashMap<>();
        Cart cart;
        if (principal == null) {
            try {
                cart = cartService.removeProductFromCart(request, productId);
                cartService.saveCartToCookie(cart, response);
                responseMessage.put("message", "Продукт успешно удален из корзины");
                return new ResponseEntity<>(responseMessage, HttpStatus.OK);
            } catch (Exception e) {
                responseMessage.put("error", e.getMessage());
                return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
            }
        } else {
            cart = cartService.removeProductFromCart(principal.getName(), productId);
            responseMessage.put("message", "Продукт успешно удален из корзины");
        }
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);

    }

    @PostMapping("update-cart")
    public ResponseEntity<Map<String, String>> updateCartItemsQuantity(@RequestBody List<CartUpdateRequest> cartItems,
                                                                       HttpServletResponse response,
                                                                       HttpServletRequest request,
                                                                       Principal principal) {
        // Ваш код для обновления количества товаров в корзине для каждого элемента в списке cartItems
        Map<String, String> responseMessage = new HashMap<>();
        try {
            if (principal == null) {
                Cart cart;
                cart = cartService.cartUpdate(request, cartItems);
                cartService.saveCartToCookie(cart, response);
                responseMessage.put("message", "Корзина успешно обновлена");
            } else {
                cartService.cartUpdate(principal.getName(), cartItems);
            }
        } catch (Exception e) {
            responseMessage.put("error", e.getMessage());
            return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

}
