package com.profi_shop.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.profi_shop.dto.CartDTO;
import com.profi_shop.exceptions.CouponException;
import com.profi_shop.exceptions.ExistException;
import com.profi_shop.exceptions.NotEnoughException;
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

import java.sql.Date;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CouponService couponService;

    private final ProductRepository productRepository;

    private final CartItemRepository cartItemRepository;

    private final ProductVariationRepository productVariationRepository;
    private final StoreHouseRepository storeHouseRepository;
    private final CartFacade cartFacade;
    private final PriceService priceService;

    private final StockRepository stockRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public CartService(CartRepository cartRepository, UserRepository userRepository, CouponService couponService, ProductRepository productRepository, CartItemRepository cartItemRepository, ProductVariationRepository productVariationRepository, StoreHouseRepository storeHouseRepository, CartFacade cartFacade, PriceService priceService, StockRepository stockRepository) {
        this.cartRepository = cartRepository;
        this.couponService = couponService;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
        this.productVariationRepository = productVariationRepository;
        this.storeHouseRepository = storeHouseRepository;
        this.cartFacade = cartFacade;
        this.priceService = priceService;
        this.stockRepository = stockRepository;
    }

    public Cart getCartByUsername(String username) {
        User user = getUserByUsername(username);
        Cart cart = cartRepository.findByUser(user).orElse(new Cart(user));
        return cartRepository.save(getActualStateOfCart(cart));
    }


    public Cart getCartByRequestCookies(HttpServletRequest request) throws ExistException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("cart")) {
                    String cartDataEncoded = cookie.getValue();
                    String decoded = new String(Base64.getDecoder().decode(cartDataEncoded));
                    System.out.println(decoded);
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

    private Cart getActualStateOfCart(Cart cart) {
        if (cart.getCoupon() != null && cart.getCoupon().isActive()) {
            for (CartItem cartItem : cart.getCartItems()) {
                Stock stock = getStockByProduct(cartItem.getProduct());
                if (stock == null) {
                    cartItem.setStockType(0);
                    cartItem.setDiscount(cart.getCoupon().getDiscount());
                } else if (stock.isActive()) {
                    if (stock.isFor_authenticated()) cartItem.setStockType(1);
                    else cartItem.setStockType(2);
                } else cartItem.setStockType(0);
                int discount = priceService.getDiscountForProductForUser(cartItem.getProduct(), true);
                if (discount == 0)
                    cartItem.setDiscount(cart.getCoupon().getDiscount());
                else
                    cartItem.setDiscount(discount);
                cartItemRepository.save(cartItem);
            }
        } else {
            if (cart.getCoupon() != null)
                cart.setCoupon(null);
            for (CartItem cartItem : cart.getCartItems()) {
                Stock stock = getStockByProduct(cartItem.getProduct());
                if (stock == null) {
                    cartItem.setStockType(0);
                } else if (stock.isActive()) {
                    if (stock.isFor_authenticated()) cartItem.setStockType(1);
                    else cartItem.setStockType(2);
                } else cartItem.setStockType(0);
                int discount = priceService.getDiscountForProductForUser(cartItem.getProduct(), true);
                cartItem.setDiscount(discount);
                cartItemRepository.save(cartItem);
            }
        }
        return cart;
    }

    public Cart addProductToCart(String username, Long productId) throws ExistException, NotEnoughException {
        Cart cart = getCartByUsername(username);
        Product product = getProductById(productId);
        if (countOfProductInStore(product) < 1) throw new NotEnoughException(product.getName(), 0);
        try {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartItem.setDiscount(priceService.getDiscountForProductForUser(product, true));
            if (cartItem.getDiscount() == 0 && cart.getCoupon() != null && cart.getCoupon().isActive()) {
                cartItem.setDiscount(cart.getCoupon().getDiscount());
            }
            List<ProductVariation> productVariations = productVariationRepository.findByParent(product);
            if (productVariations.size() == 1) {
                cartItem.setProductVariation(productVariations.get(0));
            }
            cartItemRepository.save(cartItem);
            cart.addItemToCart(cartItem);
            return cartRepository.save(cart);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ExistException(ExistException.CART_ITEM_EXIST);
        }
    }

    public Cart addProductToCartProductByVariationAndQuantity(String username, Long productId, Long variationId, Integer quantity) throws ExistException, NotEnoughException {
        Cart cart = getCartByUsername(username);
        Product product = getProductById(productId);
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartItem.setDiscount(priceService.getDiscountForProductForUser(product, true));
        if (cartItem.getDiscount() == 0 && cart.getCoupon() != null && cart.getCoupon().isActive()) {
            cartItem.setDiscount(cart.getCoupon().getDiscount());
        }
        if (variationId > 0) {
            ProductVariation productVariation = getProductVariationById(variationId);
            cartItem.setProductVariation(productVariation);
            int count = countOfProductVariationInStore(productVariation);
            if (count < quantity)
                throw new NotEnoughException(product.getName() + " ( " + productVariation.getSize() + " )", count);
        } else {
            int count = countOfProductInStore(product);
            if (count < quantity) throw new NotEnoughException(product.getName(), count);
        }
        cartItemRepository.save(cartItem);
        cart.addItemToCart(cartItem);
        return cartRepository.save(cart);
    }

    public Cart addProductToCart(HttpServletRequest request, Long productId) throws ExistException, NotEnoughException {
        Cart cart = getCartByRequestCookies(request);
        Product product = getProductById(productId);
        if (countOfProductInStore(product) < 1) throw new NotEnoughException(product.getName(), 0);
        try {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartItem.setDiscount(priceService.getDiscountForProductForUser(product, false));
            if (cartItem.getDiscount() == 0 && cart.getCoupon() != null && cart.getCoupon().isActive()) {
                cartItem.setDiscount(cart.getCoupon().getDiscount());
            }
            List<ProductVariation> productVariations = productVariationRepository.findByParent(product);
            if (productVariations.size() == 1) {
                cartItem.setProductVariation(productVariations.get(0));
            }
            cart.addItemToCart(cartItem);
            return cart;
        } catch (Exception e) {
            throw new ExistException(ExistException.CART_ITEM_EXIST);
        }
    }

    public Cart addProductToCartProductByVariationAndQuantity(HttpServletRequest request, Long productId, Long variationId, int quantity) throws ExistException, NotEnoughException {
        Cart cart = getCartByRequestCookies(request);
        Product product = getProductById(productId);
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartItem.setDiscount(priceService.getDiscountForProductForUser(product, false));
        if (cartItem.getDiscount() == 0 && cart.getCoupon() != null && cart.getCoupon().isActive()) {
            cartItem.setDiscount(cart.getCoupon().getDiscount());
        }
        if (variationId > 0) {
            ProductVariation productVariation = getProductVariationById(variationId);
            cartItem.setProductVariation(productVariation);
            int count = countOfProductVariationInStore(productVariation);
            if (count < quantity)
                throw new NotEnoughException(product.getName() + " ( " + productVariation.getSize() + " )", count);
        } else {
            int count = countOfProductInStore(product);
            if (count < quantity) throw new NotEnoughException(product.getName(), count);
        }
        cart.addItemToCart(cartItem);
        return cart;
    }

    public Cart removeProductFromCart(String username, Long productId, String size) {
        Cart cart = getCartByUsername(username);
        Product product = getProductById(productId);
        if (size.isEmpty()) {
            cart.removeProduct(product);
        } else {
            ProductVariation productVariation = productVariationRepository.findByParentAndSize(product, size).orElse(null);
            cart.removeProductVariation(productVariation);
        }
        cartRepository.save(cart);
        return cart;
    }

    public Cart removeProductFromCart(HttpServletRequest request, Long productId, String size) throws ExistException {
        Cart cart = getCartByRequestCookies(request);
        Product product = getProductById(productId);
        if (Objects.equals(size, "")) {
            cart.removeProduct(product);
        } else {
            ProductVariation productVariation = productVariationRepository.findByParentAndSize(product, size).orElse(null);
            cart.removeProductVariation(productVariation);
        }
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

    private User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username).orElseThrow(() -> new SearchException(SearchException.USER_NOT_FOUND));
    }

    public Cart cartUpdateForUnknown(HttpServletRequest request, List<CartUpdateRequest> cartItems) throws ExistException, NotEnoughException {
        Cart cart = getCartByRequestCookies(request);
        return cartUpdate(cart, cartItems);
    }

    public void cartUpdateForAuthUser(String username, List<CartUpdateRequest> cartItems) throws NotEnoughException {
        Cart cart = getCartByUsername(username);
        cartUpdate(cart, cartItems);
        cartRepository.save(cart);
    }

    private Cart cartUpdate(Cart cart, List<CartUpdateRequest> cartItems) throws NotEnoughException {
        for (CartUpdateRequest item : cartItems) {
            Product product = getProductById(item.getProductId());
            if (item.getProductVariationId() != null && item.getProductVariationId() != 0) {
                ProductVariation pv = getProductVariationById(item.getProductVariationId());
                int count = countOfProductVariationInStore(pv);
                if (count < item.getNewQuantity()) {
                    throw new NotEnoughException(product.getName() + " ( " + pv.getSize() + " )", count);
                }
            } else {
                int count = countOfProductInStore(product);
                if (count < item.getNewQuantity()) throw new NotEnoughException(product.getName(), count);
            }
            cart.quantityUp(getProductById(item.getProductId()), item.getNewQuantity());
        }
        return cart;
    }


    private ProductVariation getProductVariationById(Long variationId) {
        return productVariationRepository.findById(variationId).orElseThrow(() -> new SearchException(SearchException.PRODUCT_VARIATION_NOT_FOUND));
    }

    private Stock getStockByProduct(Product product) {
        return stockRepository.findByParticipants(product).orElse(null);
    }

    private Integer countOfProductInStore(Product product) {
        List<ProductVariation> productVariations = productVariationRepository.findByParent(product);
        int count = 0;
        for (ProductVariation productVariation : productVariations) {
            count += countOfProductVariationInStore(productVariation);
        }
        return count;
    }

    private Integer countOfProductVariationInStore(ProductVariation productVariation) {
        List<StoreHouse> storeHouses = storeHouseRepository.findStoreHouseByProduct(productVariation);
        int count = 0;
        for (StoreHouse storeHouse : storeHouses) {
            count += storeHouse.getQuantity();
        }
        return count;
    }

    public Cart applyCoupon(HttpServletRequest request, String couponCode) throws Exception {
        Coupon coupon = getCouponByCode(couponCode);
        if (!couponService.isCouponAvailable(coupon))
            throw new CouponException(CouponException.COUPON_ALREADY_USED);
        if (!coupon.isActiveRoot())
            throw new CouponException(CouponException.COUPON_ROOT_IS_INACTIVE);
        if (!coupon.isActive())
            throw new CouponException(coupon.getEnd_date());
        Cart cart = getCartByRequestCookies(request);
        if (cart.isCouponApplicable()) {
            for (CartItem cartItem : cart.getCartItems())
                if (cartItem.getDiscount() == 0)
                    cartItem.setDiscount(coupon.getDiscount());
            cart.setCoupon(coupon);
            return cart;
        } else
            throw new CouponException(CouponException.COUPON_NOT_APPLICABLE_TO_DISCOUNTED_PRODUCTS);
    }

    public Cart applyCoupon(String username, String couponCode) throws Exception {
        Coupon coupon = getCouponByCode(couponCode);
        if (!couponService.isCouponAvailable(coupon))
            throw new CouponException(CouponException.COUPON_ALREADY_USED);
        if (!coupon.isActiveRoot())
            throw new CouponException(CouponException.COUPON_ROOT_IS_INACTIVE);
        if (!coupon.isActive())
            throw new CouponException(coupon.getEnd_date());
        Cart cart = getCartByUsername(username);
        if (cart.isCouponApplicable()) {
            couponService.applyCouponToCart(cart, coupon, true);
        } else
            throw new CouponException(CouponException.COUPON_NOT_APPLICABLE_TO_DISCOUNTED_PRODUCTS);
        return cart;
    }

    private Coupon getCouponByCode(String couponCode) {
        return couponService.findByActivationCode(couponCode);
    }

    public void removeCoupon(String name) {
        Cart cart = getCartByUsername(name);
        cart.setCoupon(null);
        cartRepository.save(cart);
    }

    public Cart removeCoupon(HttpServletRequest request) throws ExistException {
        Cart cart = getCartByRequestCookies(request);
        cart.setCoupon(null);
        return cart;
    }

    public int getCartCount(HttpServletRequest request) throws ExistException {
        Cart cart = getCartByRequestCookies(request);
        return cart.getCartItems().size();
    }

    public int getCartCount(String username) {
        Cart cart = getCartByUsername(username);
        return cart.getCartItems().size();
    }

    public void clearCart(Cart cart) {
        if (cart.getCoupon() != null) {
            cart.setCoupon(null);
            cartRepository.save(cart);
        }
        cartItemRepository.deleteAll(cart.getCartItems());
    }
}
