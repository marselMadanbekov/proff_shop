package com.profi_shop.services;

import com.profi_shop.exceptions.SearchException;
import com.profi_shop.model.*;
import com.profi_shop.repositories.*;
import com.profi_shop.settings.Templates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Service
public class CouponService {
    private final CouponRepository couponRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CouponTemplateRepository couponTemplateRepository;
    private final UserRepository userRepository;

    @Autowired
    public CouponService(CouponRepository couponRepository, CartItemRepository cartItemRepository, OrderRepository orderRepository, CartRepository cartRepository, CouponTemplateRepository couponTemplateRepository, UserRepository userRepository) {
        this.couponRepository = couponRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.couponTemplateRepository = couponTemplateRepository;
        this.userRepository = userRepository;
    }

    public void createCoupon(Order order, CouponTemplate couponTemplate){
        Coupon coupon = new Coupon();
        coupon.setEnd_date(Date.valueOf(LocalDate.now().plusDays(couponTemplate.getDuration())));
        coupon.setOwner(order.getUser());
        coupon.setRoot(order);
        coupon.setParent(couponTemplate);
        coupon.setDiscount(couponTemplate.getDiscount());
        couponRepository.save(coupon);
        setActivationCodeToCoupon(coupon);
    }

    public void createCouponIfNeeded(Order order){
        if(order.getUser() == null) return;
        Coupon coupon = couponRepository.findByRoot(order).orElse(null);
        if(coupon != null) return;
        int amount = 0;
        if(order.getShipment() == null) amount = order.getTotalPrice();
        else amount = order.getTotalPrice() - order.getShipment().getCost();

        CouponTemplate couponTemplate = couponTemplateRepository.findTopByMinAmountLessThanOrderByMinAmountDesc(amount).orElse(null);
        if(couponTemplate == null)  return;

        createCoupon(order,couponTemplate);
    }

    public boolean isCouponAvailable(Coupon coupon){
        return orderRepository.findByCoupon(coupon) == null;
    }
    public Cart applyCouponToCart(Cart cart,Coupon coupon, boolean saving){
        for(CartItem cartItem : cart.getCartItems()){
            if(cartItem.getDiscount() == 0){
                cartItem.setDiscount(coupon.getDiscount());
                System.out.println("setting discount " + coupon.getDiscount());
                cartItemRepository.save(cartItem);
                System.out.println("after set discount " + cartItem.getDiscount());
            }
        }
        cart.setCoupon(coupon);
        if(saving)  cartRepository.save(cart);
        return cart;
    }
    private void setActivationCodeToCoupon(Coupon coupon){
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        String activationCode = Templates.COUPON_PREFIX + alphabet.charAt(random.nextInt(36)) + coupon.getId() + alphabet.charAt(random.nextInt(36));
        coupon.setActivationCode(activationCode);
        couponRepository.save(coupon);
    }

    public Coupon findByActivationCode(String couponCode) {
        return couponRepository.findByActivationCode(couponCode).orElseThrow(() -> new SearchException(SearchException.COUPON_NOT_FOUND));
    }

    public List<Coupon> findCouponsByUsername(String name) {
        User user = getUserByUsername(name);
        return couponRepository.findByOwner(user);
    }

    private User getUserByUsername(String name) {
        return userRepository.findUserByUsername(name).orElseThrow(() -> new SearchException(SearchException.USER_NOT_FOUND));
    }
}
