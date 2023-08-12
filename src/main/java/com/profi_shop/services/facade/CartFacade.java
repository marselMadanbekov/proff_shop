package com.profi_shop.services.facade;

import com.profi_shop.dto.CartDTO;
import com.profi_shop.exceptions.SearchException;
import com.profi_shop.model.Cart;
import com.profi_shop.model.CartItem;
import com.profi_shop.model.Product;
import com.profi_shop.repositories.ProductRepository;
import com.profi_shop.services.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartFacade {
    private final ProductFacade productFacade;
    private final ProductRepository productRepository;

    private final PriceService priceService;

    @Autowired
    public CartFacade(ProductFacade productFacade, ProductRepository productRepository, PriceService priceService) {
        this.productFacade = productFacade;
        this.productRepository = productRepository;
        this.priceService = priceService;
    }

    public CartDTO cartToCartDTO(Cart cart){
        CartDTO cartDTO = new CartDTO();
        for(CartItem cartItem : cart.getCartItems()){
            cartDTO.addProduct(cartItem.getProduct().getId(), cartItem.getQuantity());
        }
        return cartDTO;
    }

    public Cart cartDTOToCart(CartDTO cartDTO){
        Cart cart = new Cart();
        for(Long productId : cartDTO.getProduct_quantity().keySet()){
            Product product = getProductByProductId(productId);
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(cartDTO.getProduct_quantity().get(productId));
            cartItem.setDiscount(priceService.getDiscountForProductForUser(product,false));
            cart.addItemToCart(cartItem);
        }
        return cart;
    }



    private Product getProductByProductId(Long productId){
        return productRepository.findById(productId).orElseThrow(() -> new SearchException(SearchException.PRODUCT_NOT_FOUND));
    }
}
