package com.profi_shop.services;

import com.profi_shop.exceptions.ExistException;
import com.profi_shop.exceptions.InvalidDataException;
import com.profi_shop.exceptions.NotEnoughException;
import com.profi_shop.exceptions.SearchException;
import com.profi_shop.model.*;
import com.profi_shop.model.enums.OrderStatus;
import com.profi_shop.model.enums.ShipmentType;
import com.profi_shop.model.requests.OrderRequest;
import com.profi_shop.repositories.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductVariationRepository productVariationRepository;
    private final ShipmentRepository shipmentRepository;
    private final StoreRepository storeRepository;
    private final NotificationService notificationService;
    private final StoreHouseRepository storeHouseRepository;
    private final UserRepository userRepository;
    private final CartService cartService;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, ProductVariationRepository productVariationRepository, ShipmentRepository shipmentRepository, StoreRepository storeRepository, NotificationService notificationService, StoreHouseRepository storeHouseRepository, UserRepository userRepository, CartService cartService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productVariationRepository = productVariationRepository;
        this.shipmentRepository = shipmentRepository;
        this.storeRepository = storeRepository;
        this.notificationService = notificationService;
        this.storeHouseRepository = storeHouseRepository;
        this.userRepository = userRepository;
        this.cartService = cartService;
    }

    public void createOrderByUnknown(OrderRequest orderRequest, HttpServletRequest request) throws InvalidDataException, NotEnoughException, ExistException {
        Cart cart = cartService.getCartByRequestCookies(request);
        Order order = createBaseOrder(orderRequest,cart);
        orderRepository.save(order);
        notificationService.createLocalNewOrderNotification(order.getStore().getAdmin(), order.getId());
    }
    public void createOrderByUsername(OrderRequest orderRequest, String username) throws InvalidDataException, NotEnoughException {
        User user = getUserByUsername(username);
        Cart cart = cartService.getCartByUsername(username);
        Order order = createBaseOrder(orderRequest, cart);
        order.setUser(user);
        orderRepository.save(order);
        notificationService.createLocalNewOrderNotification(order.getStore().getAdmin(), order.getId());

    }
    private Order createBaseOrder(OrderRequest orderRequest,Cart cart) throws InvalidDataException, NotEnoughException {
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
            if(cartItem.getProductVariation() != null){
                int count = countOfProductVariationInStore(cartItem.getProductVariation());
                if(count < cartItem.getQuantity())  throw new NotEnoughException(cartItem.getProduct().getName() + " ( " + cartItem.getProductVariation().getProductSize() + " ) ",count);
            }else{
                int count = countOfProductInStore(cartItem.getProduct());
                if(count < cartItem.getQuantity())  throw new NotEnoughException(cartItem.getProduct().getName(),count);
            }
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setProductVariation(cartItem.getProductVariation());
            orderItem.setPrice(cartItem.getAmount());
            orderItemRepository.save(orderItem);
            order.addOrderItem(orderItem);
        }
        totalPrice += cart.cartAmount();
        order.setTotal_price(totalPrice);
        order.setStore(chooseStoreToOrder());
        return orderRepository.save(order);
    }
    private User getUserByUsername(String username){
        return userRepository.findUserByUsername(username).orElseThrow(() -> new SearchException(SearchException.USER_NOT_FOUND));
    }

    private Store chooseStoreToOrder(){
        List<Store> stores = storeRepository.findAll();
        return stores.get(new Random().nextInt(stores.size()));
    }

    private Integer countOfProductInStore(Product product){
        List<ProductVariation> productVariations = productVariationRepository.findByParent(product);
        int count = 0;
        for(ProductVariation productVariation: productVariations){
            count+=countOfProductVariationInStore(productVariation);
        }
        return count;
    }

    private Integer countOfProductVariationInStore(ProductVariation productVariation){
        List<StoreHouse> storeHouses = storeHouseRepository.findStoreHouseByProduct(productVariation);
        int count = 0;
        for(StoreHouse storeHouse: storeHouses){
            count += storeHouse.getQuantity();
        }
        return count;
    }
}
