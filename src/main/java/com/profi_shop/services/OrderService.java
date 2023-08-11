package com.profi_shop.services;

import com.profi_shop.exceptions.InvalidDataException;
import com.profi_shop.exceptions.SearchException;
import com.profi_shop.model.*;
import com.profi_shop.model.enums.OrderStatus;
import com.profi_shop.model.enums.ShipmentType;
import com.profi_shop.model.requests.OrderRequest;
import com.profi_shop.repositories.OrderItemRepository;
import com.profi_shop.repositories.OrderRepository;
import com.profi_shop.repositories.ShipmentRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShipmentRepository shipmentRepository;

    private final CartService cartService;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, ShipmentRepository shipmentRepository, CartService cartService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.shipmentRepository = shipmentRepository;
        this.cartService = cartService;
    }

    public Order createOrderByUnknown(OrderRequest orderRequest, HttpServletRequest request){
        Order order = new Order();
        Cart cart = cartService.getCartByRequestCookies(request);
        int totalPrice = 0;
        if(cart.getCartItems().isEmpty()){
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
            orderItem.setPrice(cartItem.getAmount());
            orderItemRepository.save(orderItem);
            order.addOrderItem(orderItem);
        }

        totalPrice += cart.cartAmount();
        order.setTotal_price(totalPrice);

        return orderRepository.save(order);
    }

//    public Order createOrderByUser(OrderRequest orderRequest, String username){
//
//    }
}
