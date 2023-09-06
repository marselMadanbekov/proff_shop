package com.profi_shop.controllers.generalControllers;

import com.profi_shop.exceptions.CouponException;
import com.profi_shop.exceptions.ExistException;
import com.profi_shop.model.Cart;
import com.profi_shop.model.CartItem;
import com.profi_shop.model.Category;
import com.profi_shop.model.requests.CartUpdateRequest;
import com.profi_shop.services.CartService;
import com.profi_shop.services.CategoryService;
import com.profi_shop.services.ShipmentService;
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
    private final CategoryService categoryService;
    private final ShipmentService shipmentService;

    @Autowired
    public CartController(CartService cartService, CategoryService categoryService, ShipmentService shipmentService) {
        this.cartService = cartService;
        this.categoryService = categoryService;
        this.shipmentService = shipmentService;
    }

    @GetMapping("")
    public String cart(Principal principal,
                       HttpServletRequest request,
                       HttpServletResponse response,
                       Model model) throws ExistException, CouponException {
        Cart cart;
        if (principal == null) {
            cart = cartService.getCartByRequestCookies(request);
            cartService.saveCartToCookie(cart, response);
        } else {
            cart = cartService.getCartByUsername(principal.getName());
        }

        List<Category> categories = categoryService.getMainCategories();
        List<String> states = shipmentService.getUniqueStates();
        List<String> towns = shipmentService.getUniqueTowns();

        model.addAttribute("cart", cart);
        model.addAttribute("states", states);
        model.addAttribute("towns", towns);
        model.addAttribute("categories", categories);
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

    @PostMapping("/addByVariation")
    public ResponseEntity<Map<String, String>> addToCartByVariationQuantity(@RequestParam("productId") Long productId,
                                                                            @RequestParam("variationId") Long variationId,
                                                                            @RequestParam("quantity") Integer quantity,
                                                                            HttpServletRequest request,
                                                                            HttpServletResponse response,
                                                                            Principal principal) {
        Map<String, String> responseMessage = new HashMap<>();
        Cart cart;
        try {
            if (principal == null) {
                cart = cartService.addProductToCartProductByVariationAndQuantity(request, productId, variationId, quantity);
                cartService.saveCartToCookie(cart, response);
                responseMessage.put("message", "Продукт успешно добавлен в корзину");
            } else {
                cart = cartService.addProductToCartProductByVariationAndQuantity(principal.getName(), productId, variationId, quantity);
                responseMessage.put("message", "Продукт успешно добавлен в корзину");
            }
        } catch (Exception e) {
            responseMessage.put("error", e.getMessage());
            return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @PostMapping("/apply-coupon")
    public ResponseEntity<Map<String, String>> applyCoupon(@RequestParam String couponCode,
                                                           HttpServletRequest request,
                                                           HttpServletResponse responsem,
                                                           Principal principal) {
        Map<String, String> response = new HashMap<>();
        Cart cart = null;
        try {
            if (principal == null) {
                cart = cartService.applyCoupon(request, couponCode);
                cartService.saveCartToCookie(cart, responsem);
                response.put("message", "Купон успешно применен!");
            } else {
                cart = cartService.applyCoupon(principal.getName(), couponCode);
                response.put("message", "Купон успешно применен!");
            }
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/remove-coupon")
    public ResponseEntity<Map<String, String>> removeCoupon(HttpServletRequest request,
                                                            HttpServletResponse responsem,
                                                            Principal principal) {
        Map<String, String> response = new HashMap<>();
        Cart cart = null;
        try {
            if (principal == null) {
                cart = cartService.removeCoupon(request);
                cartService.saveCartToCookie(cart, responsem);
                response.put("message", "Купон успешно удален!");
            } else {
                cartService.removeCoupon(principal.getName());
                response.put("message", "Купон успешно удален!");
            }
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/remove")
    public ResponseEntity<Map<String, String>> removeFromCart(@RequestParam("productId") Long productId,
                                                              @RequestParam("size") String size,
                                                              HttpServletRequest request,
                                                              HttpServletResponse response,
                                                              Principal principal) throws CouponException {
        Map<String, String> responseMessage = new HashMap<>();
        Cart cart;
        if (principal == null) {
            try {
                cart = cartService.removeProductFromCart(request, productId, size);
                cartService.saveCartToCookie(cart, response);
                responseMessage.put("message", "Продукт успешно удален из корзины");
                return new ResponseEntity<>(responseMessage, HttpStatus.OK);
            } catch (Exception e) {
                responseMessage.put("error", e.getMessage());
                return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
            }
        } else {
            cart = cartService.removeProductFromCart(principal.getName(), productId, size);
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
                cart = cartService.cartUpdateForUnknown(request, cartItems);
                cartService.saveCartToCookie(cart, response);
            } else {
                cartService.cartUpdateForAuthUser(principal.getName(), cartItems);
            }
            responseMessage.put("message", "Корзина успешно обновлена");
        } catch (Exception e) {
            responseMessage.put("error", e.getMessage());
            return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

}
