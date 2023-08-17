package com.profi_shop.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.profi_shop.dto.CartDTO;
import com.profi_shop.exceptions.SearchException;
import com.profi_shop.model.*;
import com.profi_shop.model.requests.CartUpdateRequest;
import com.profi_shop.repositories.*;
import com.profi_shop.services.facade.CartFacade;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;

@Service
public class CartService {
    private final CartRepository cartRepository;

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    private final CartItemRepository cartItemRepository;

    private final ProductVariationRepository productVariationRepository;

    private final CartFacade cartFacade;
    private final PriceService priceService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    public CartService(CartRepository cartRepository, UserRepository userRepository, ProductRepository productRepository, CartItemRepository cartItemRepository, ProductVariationRepository productVariationRepository, CartFacade cartFacade, PriceService priceService) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
        this.productVariationRepository = productVariationRepository;
        this.cartFacade = cartFacade;
        this.priceService = priceService;
    }

    public Cart getCartByUsername(String username){
        User user = getUserByUsername(username);
        Cart cart = cartRepository.findByUser(user).orElse(new Cart(user));
        if(cart.needForUpdating()){
            System.out.println("hello there is my need to update");
            for(CartItem cartItem : cart.getCartItems()){
                cartItem.setDiscount(priceService.getDiscountForProductForUser(cartItem.getProduct(),true));
                cartItemRepository.save(cartItem);
            }
        }
        return cartRepository.save(cart);
    }

    public Cart getCartByRequestCookies(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("cart")) {
                    String cartDataEncoded = cookie.getValue();
                    String decoded = new String(Base64.getDecoder().decode(cartDataEncoded));
                    try {
                        return cartFacade.cartDTOToCart(objectMapper.readValue(decoded, CartDTO.class));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return new Cart();
    }
    public Cart addProductToCart(String username, Long productId) {
        Cart cart = getCartByUsername(username);
        Product product = getProductById(productId);
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(1);
        cartItem.setDiscount(priceService.getDiscountForProductForUser(product,true));
        cartItemRepository.save(cartItem);
        cart.addItemToCart(cartItem);
        return cartRepository.save(cart);
    }
    public Cart addProductToCartProductByVariationAndQuantity(String username, Long productId, Long variationId, Integer quantity) {
        Cart cart = getCartByUsername(username);
        Product product = getProductById(productId);
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartItem.setDiscount(priceService.getDiscountForProductForUser(product,true));
        if(variationId > 0) {
            ProductVariation productVariation = getProductVariationById(variationId);
            cartItem.setProductVariation(productVariation);
        }
        cartItemRepository.save(cartItem);
        cart.addItemToCart(cartItem);
        return cartRepository.save(cart);
    }
    public Cart addProductToCart(HttpServletRequest request, Long productId) {
        Cart cart = getCartByRequestCookies(request);
        Product product = getProductById(productId);
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(1);
        cartItem.setDiscount(priceService.getDiscountForProductForUser(product,false));
        cart.addItemToCart(cartItem);
        return cart;
    }
    public Cart addProductToCartProductByVariationAndQuantity(HttpServletRequest request, Long productId, Long variationId, int quantity) {
        Cart cart = getCartByRequestCookies(request);
        Product product = getProductById(productId);
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartItem.setDiscount(priceService.getDiscountForProductForUser(product,false));
        if(variationId > 0) {
            ProductVariation productVariation = getProductVariationById(variationId);
            cartItem.setProductVariation(productVariation);
        }
        cart.addItemToCart(cartItem);
        return cart;
    }

    public Cart removeProductFromCart(String username, Long productId) {
        Cart cart = getCartByUsername(username);
        Product product = getProductById(productId);
        cart.removeProduct(product);
        cartRepository.save(cart);
        return cart;
    }
    public Cart removeProductFromCart(HttpServletRequest request, Long productId) {
        Cart cart = getCartByRequestCookies(request);
        Product product = getProductById(productId);
        cart.removeProduct(product);
        return cart;
    }
    private Product getProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new SearchException(SearchException.PRODUCT_NOT_FOUND));
    }

    public void saveCartToCookie(Cart cart, HttpServletResponse response) {
        try {

            String serializedCart = objectMapper.writeValueAsString(cartFacade.cartToCartDTO(cart));
            String encodedJson = Base64.getEncoder().encodeToString(serializedCart.getBytes());
            System.out.println(encodedJson);
            Cookie cartCookie = new Cookie("cart", encodedJson);
            cartCookie.setMaxAge(4 * 60 * 60); // Например, на неделю
            cartCookie.setPath("/");
            response.addCookie(cartCookie);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private User getUserByUsername(String username){
        return userRepository.findUserByUsername(username).orElseThrow(() -> new SearchException(SearchException.USER_NOT_FOUND));
    }

    public Cart cartUpdate(HttpServletRequest request, List<CartUpdateRequest> cartItems) {
        Cart cart = getCartByRequestCookies(request);
        for(CartUpdateRequest item : cartItems){
            cart.quantityUp(getProductById(item.getProductId()), item.getNewQuantity());
        }
        return cart;
    }

    public Cart cartUpdate(String username, List<CartUpdateRequest> cartItems){
        Cart cart = getCartByUsername(username);
        for(CartUpdateRequest item : cartItems){
            cart.quantityUp(getProductById(item.getProductId()), item.getNewQuantity());
        }
        return cartRepository.save(cart);
    }




    private ProductVariation getProductVariationById(Long variationId) {
        return productVariationRepository.findById(variationId).orElseThrow(() -> new SearchException(SearchException.PRODUCT_VARIATION_NOT_FOUND));
    }
}
