package com.profi_shop.services;

import com.profi_shop.exceptions.InvalidDataException;
import com.profi_shop.exceptions.SearchException;
import com.profi_shop.model.*;
import com.profi_shop.model.enums.OrderStatus;
import com.profi_shop.model.enums.ShipmentType;
import com.profi_shop.model.requests.OrderRequest;
import com.profi_shop.repositories.*;
import com.profi_shop.validations.Validator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShipmentRepository shipmentRepository;
    private final StoreRepository storeRepository;
    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final CartService cartService;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, ShipmentRepository shipmentRepository, StoreRepository storeRepository, NotificationService notificationService, UserRepository userRepository, CartService cartService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.shipmentRepository = shipmentRepository;
        this.storeRepository = storeRepository;
        this.notificationService = notificationService;
        this.userRepository = userRepository;
        this.cartService = cartService;
    }

    public void createOrderByUnknown(OrderRequest orderRequest, HttpServletRequest request) throws InvalidDataException {
        Order order = new Order();
        Cart cart = cartService.getCartByRequestCookies(request);
        int totalPrice = 0;
        if(cart.getCartItems().size() == 0){
            throw new InvalidDataException(InvalidDataException.EMPTY_CART);
        }
        order.setFirstname(Validator.validFirstname(orderRequest.getFirstname()));
        order.setLastname(Validator.validLastname(orderRequest.getLastname()));
        order.setPhone_number(Validator.validNumber(orderRequest.getPhone_number()));
        order.setEmail(orderRequest.getEmail());
        if(orderRequest.isToShip()){
            Shipment shipment = shipmentRepository.findByTownAndState(orderRequest.getTown(), orderRequest.getState()).orElseThrow(() -> new SearchException(SearchException.SHIPMENT_NOT_FOUND));
            order.setShipment(shipment);
            totalPrice += shipment.getCost();
            order.setShipmentType(ShipmentType.SHIPMENT);
        }else {
            order.setShipmentType(ShipmentType.SELF);
        }
        order.setStatus(OrderStatus.REQUEST);

        for(CartItem cartItem : cart.getCartItems()){
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getAmount());
            orderItemRepository.save(orderItem);
            order.addOrderItem(orderItem);
        }
        order.setStore(chooseStoreToOrder());
        totalPrice += cart.cartAmount();
        order.setTotal_price(totalPrice);
        orderRepository.save(order);
        notificationService.createLocalNewOrderNotification(order.getStore().getAdmin(), order.getId());
    }
    public void createOrderByUsername(OrderRequest orderRequest, String username) throws InvalidDataException {
        User user = getUserByUsername(username);
        Cart cart = cartService.getCartByUsername(username);
        Order order = new Order();

        int totalPrice = 0;
        if(cart.getCartItems().size() == 0){
            throw new InvalidDataException(InvalidDataException.EMPTY_CART);
        }
        if(orderRequest.isToShip()){
            Shipment shipment = shipmentRepository.findByTownAndState(orderRequest.getTown(), orderRequest.getState()).orElseThrow(() -> new SearchException(SearchException.SHIPMENT_NOT_FOUND));
            order.setShipment(shipment);
            totalPrice += shipment.getCost();
            order.setShipmentType(ShipmentType.SHIPMENT);
        }else {
            order.setShipmentType(ShipmentType.SELF);
        }
        order.setStatus(OrderStatus.REQUEST);

        for(CartItem cartItem : cart.getCartItems()){
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setProductVariation(cartItem.getProductVariation());
            orderItem.setPrice(cartItem.getAmount());
            orderItemRepository.save(orderItem);
            order.addOrderItem(orderItem);
        }
        order.setStore(chooseStoreToOrder());
        order.setUser(user);
        totalPrice += cart.cartAmount();
        order.setTotal_price(totalPrice);

        orderRepository.save(order);
        notificationService.createLocalNewOrderNotification(order.getStore().getAdmin(), order.getId());

    }

    private User getUserByUsername(String username){
        return userRepository.findUserByUsername(username).orElseThrow(() -> new SearchException(SearchException.USER_NOT_FOUND));
    }

    private Store chooseStoreToOrder(){
        List<Store> stores = storeRepository.findAll();
        return stores.get(new Random().nextInt(stores.size()));
    }
}
