package com.profi_shop.services.facade;

import com.profi_shop.dto.CartDTO;
import com.profi_shop.dto.CartItemDTO;
import com.profi_shop.exceptions.ExistException;
import com.profi_shop.exceptions.SearchException;
import com.profi_shop.model.*;
import com.profi_shop.repositories.CouponRepository;
import com.profi_shop.repositories.ProductRepository;
import com.profi_shop.repositories.ProductVariationRepository;
import com.profi_shop.repositories.StockRepository;
import com.profi_shop.services.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartFacade {
    private final ProductVariationRepository productVariationRepository;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;
    private final PriceService priceService;
    private final CouponRepository couponRepository;

    @Autowired
    public CartFacade(ProductVariationRepository productVariationRepository, ProductRepository productRepository, StockRepository stockRepository, PriceService priceService, CouponRepository couponRepository) {
        this.productVariationRepository = productVariationRepository;
        this.productRepository = productRepository;
        this.stockRepository = stockRepository;
        this.priceService = priceService;
        this.couponRepository = couponRepository;
    }

    public CartDTO cartToCartDTO(Cart cart){
        CartDTO cartDTO = new CartDTO();
        for(CartItem cartItem : cart.getCartItems()){
            CartItemDTO item = new CartItemDTO();
            item.setProductId(cartItem.getProduct().getId());
            item.setQuantity(cartItem.getQuantity());
            item.setSize(cartItem.getProductVariation() == null ? null : cartItem.getProductVariation().getSize());
            cartDTO.addItem(item);
        }
        if(cart.getCoupon() != null)
            cartDTO.setCouponId(cart.getCoupon().getId());
        else
            cartDTO.setCouponId(0L);
        return cartDTO;
    }

    public Cart cartDTOToCart(CartDTO cartDTO) throws ExistException {
        Cart cart = new Cart();
        Coupon coupon = couponRepository.findById(cartDTO.getCouponId()).orElse(null);
        for(CartItemDTO cartItemDTO : cartDTO.getCartItems()){
            Product product = getProductByProductId(cartItemDTO.getProductId());
            Stock stock = getStockByProduct(product);
            CartItem cartItem = new CartItem();
            if (stock == null)  cartItem.setStockType(0);
            else if (stock.isActive()) {
                if (stock.isFor_authenticated()) cartItem.setStockType(1);
                else cartItem.setStockType(2);
            }
            else cartItem.setStockType(0);
            cartItem.setProduct(product);
            cartItem.setQuantity(cartItemDTO.getQuantity());
            if(cartItemDTO.getSize() != null){
                cartItem.setProductVariation(getProductVariationByProductAndSize(product,cartItemDTO.getSize()));
            }
            cartItem.setDiscount(priceService.getDiscountForProductForUser(product,false));
            if(cartItem.getDiscount() == 0 && coupon != null && coupon.isActive())
                cartItem.setDiscount(coupon.getDiscount());
            cart.addItemToCart(cartItem);
        }
        if(cartDTO.getCouponId() != 0L) {
            cart.setCoupon(coupon);
        }
        return cart;
    }

    private Stock getStockByProduct(Product product) {
        return stockRepository.findByParticipants(product).orElse(null);
    }


    private Product getProductByProductId(Long productId){
        return productRepository.findById(productId).orElseThrow(() -> new SearchException(SearchException.PRODUCT_NOT_FOUND));
    }

    private ProductVariation getProductVariationByProductAndSize(Product product, String productSize){
        return productVariationRepository.findByParentAndSize(product, productSize).orElseThrow(() -> new SearchException(SearchException.PRODUCT_VARIATION_NOT_FOUND));
    }
}
