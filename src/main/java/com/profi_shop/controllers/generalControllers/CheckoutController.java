package com.profi_shop.controllers.generalControllers;

import com.profi_shop.model.Cart;
import com.profi_shop.model.requests.OrderRequest;
import com.profi_shop.services.CartService;
import com.profi_shop.services.OrderService;
import com.profi_shop.services.ShipmentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    private final ShipmentService shipmentService;
    private final OrderService orderService;
    private final CartService cartService;

    @Autowired
    public CheckoutController(ShipmentService shipmentService, OrderService orderService, CartService cartService) {
        this.shipmentService = shipmentService;
        this.orderService = orderService;
        this.cartService = cartService;
    }


    @GetMapping("/towns")
    public ResponseEntity<List<String>> getTowns(@RequestParam("state") String state) {
        try {
            List<String> towns = shipmentService.getTownsByState(state);
            return new ResponseEntity<>(towns, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/calculateShipping")
    public ResponseEntity<Integer> calculateShipping(@RequestParam("state") String state,
                                                     @RequestParam("town") String town) {
        try {
            Integer cost = shipmentService.calculateShipping(state, town);
            return new ResponseEntity<>(cost, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(0, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/order")
    public ResponseEntity<Map<String, String>> submitOrder(@ModelAttribute OrderRequest orderDetails,
                                                           HttpServletRequest request,
                                                           Principal principal) {
        Map<String, String> response = new HashMap<>();
        try {
            if(principal == null){
                orderService.createOrderByUnknown(orderDetails,request);
            }else{
                orderService.createOrderByUsername(orderDetails,principal.getName());
            }
            response.put("message", "Заказ успешно оформлен. Ожидайте скоро с вами свяжутся операторы!");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
