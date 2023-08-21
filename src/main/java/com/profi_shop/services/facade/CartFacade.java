package com.profi_shop.services.facade;

import com.profi_shop.dto.CartDTO;
import com.profi_shop.dto.CartItemDTO;
import com.profi_shop.exceptions.ExistException;
import com.profi_shop.exceptions.SearchException;
import com.profi_shop.model.*;
import com.profi_shop.model.enums.ProductSize;
import com.profi_shop.repositories.ProductRepository;
import com.profi_shop.repositories.ProductVariationRepository;
import com.profi_shop.repositories.StockRepository;
import com.profi_shop.services.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartFacade {
    private final ProductFacade productFacade;
    private final ProductVariationRepository productVariationRepository;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;
    private final PriceService priceService;

    @Autowired
    public CartFacade(ProductFacade productFacade, ProductVariationRepository productVariationRepository, ProductRepository productRepository, StockRepository stockRepository, PriceService priceService) {
        this.productFacade = productFacade;
        this.productVariationRepository = productVariationRepository;
        this.productRepository = productRepository;
        this.stockRepository = stockRepository;
        this.priceService = priceService;
    }

    public CartDTO cartToCartDTO(Cart cart){
        CartDTO cartDTO = new CartDTO();
        for(CartItem cartItem : cart.getCartItems()){
            CartItemDTO item = new CartItemDTO();
            item.setProductId(cartItem.getProduct().getId());
            item.setQuantity(cartItem.getQuantity());
            item.setSize((cartItem.getProductVariation() == null) ? null: cartItem.getProductVariation().getProductSize().ordinal());
            cartDTO.addItem(item);
        }
        return cartDTO;
    }

    public Cart cartDTOToCart(CartDTO cartDTO) throws ExistException {
        Cart cart = new Cart();
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
                cartItem.setProductVariation(getProductVariationByProductAndSize(product,ProductSize.values()[cartItemDTO.getSize()] ));
            }
            cartItem.setDiscount(priceService.getDiscountForProductForUser(product,false));
            cart.addItemToCart(cartItem);
        }
        return cart;
    }

    private Stock getStockByProduct(Product product) {
        return stockRepository.findByParticipants(product).orElse(null);
    }


    private Product getProductByProductId(Long productId){
        return productRepository.findById(productId).orElseThrow(() -> new SearchException(SearchException.PRODUCT_NOT_FOUND));
    }

    private ProductVariation getProductVariationByProductAndSize(Product product, ProductSize productSize){
        return productVariationRepository.findByParentAndProductSize(product, productSize).orElseThrow(() -> new SearchException(SearchException.PRODUCT_VARIATION_NOT_FOUND));
    }
}
