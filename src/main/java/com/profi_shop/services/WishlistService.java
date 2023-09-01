package com.profi_shop.services;

import com.profi_shop.exceptions.ExistException;
import com.profi_shop.exceptions.SearchException;
import com.profi_shop.model.Product;
import com.profi_shop.model.User;
import com.profi_shop.model.Wishlist;
import com.profi_shop.repositories.ProductRepository;
import com.profi_shop.repositories.UserRepository;
import com.profi_shop.repositories.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class WishlistService {
    private final ProductRepository productRepository;
    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;

    @Autowired
    public WishlistService(ProductRepository productRepository, WishlistRepository wishlistRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.wishlistRepository = wishlistRepository;
        this.userRepository = userRepository;
    }

    public void addProductToWishlist(Long productId, Principal principal) throws ExistException {
        try {
            Product product = getProductById(productId);
            User user = getUserByUsername(principal.getName());
            Wishlist wishlist = wishlistRepository.findByCustomer(user).orElse(new Wishlist(user));
            wishlist.addProduct(product);
            wishlistRepository.save(wishlist);
        }catch (Exception e){
            throw new ExistException(ExistException.WISHLIST_CONTAINS_PRODUCT);
        }
    }
    public void removeProduct(Long productId, Principal principal) {
        Product product = getProductById(productId);
        User user = getUserByUsername(principal.getName());
        Wishlist wishlist = wishlistRepository.findByCustomer(user).orElseThrow(() -> new SearchException(SearchException.WISHLIST_NOT_FOUND));
        wishlist.removeProduct(product);
        wishlistRepository.save(wishlist);
    }

    public Wishlist getByUsername(String username){
        return wishlistRepository.findByCustomer(getUserByUsername(username)).orElseThrow(() -> new SearchException(SearchException.WISHLIST_NOT_FOUND));
    }

    private User getUserByUsername(String username){
        return userRepository.findUserByUsername(username).orElseThrow(() -> new SearchException(SearchException.USER_NOT_FOUND));
    }

    private Product getProductById(Long productId){
        return productRepository.findById(productId).orElseThrow(() -> new SearchException(SearchException.PRODUCT_NOT_FOUND));
    }

    public int getWishlistCountByUsername(String name) {
        Wishlist wishlist = getByUsername(name);
        return wishlist.getProducts().size();
    }
}
