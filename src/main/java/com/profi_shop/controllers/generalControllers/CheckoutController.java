package com.profi_shop.controllers.generalControllers;

import com.profi_shop.model.Cart;
import com.profi_shop.model.requests.OrderRequest;
import com.profi_shop.services.CartService;
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
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    private final ShipmentService shipmentService;
    private final CartService cartService;

    @Autowired
    public CheckoutController(ShipmentService shipmentService, CartService cartService) {
        this.shipmentService = shipmentService;
        this.cartService = cartService;
    }

    @GetMapping("")
    public String checkout(@RequestParam(value = "state", required = false) Optional<String> state,
                           @RequestParam(value = "town", required = false) Optional<String> town,
                           Model model,
                           Principal principal,
                           HttpServletRequest request,
                           HttpServletResponse response) {
        Cart cart;
        try {
            if (principal == null) {
                cart = cartService.getCartByRequestCookies(request);
                cartService.saveCartToCookie(cart, response);
            } else {
                cart = cartService.getCartByUsername(principal.getName());
            }
        }catch (Exception e){
            cart = new Cart();
        }


        List<String> states = shipmentService.getUniqueStates();
        List<String> towns = shipmentService.getUniqueTowns();

        model.addAttribute("cart", cart);
        model.addAttribute("states", states);
        model.addAttribute("towns", towns);
        return "shop/checkout";
    }

    @GetMapping("/towns")
    public ResponseEntity<List<String>> getTowns(@RequestParam("state") String state){
        try{
            List<String> towns = shipmentService.getTownsByState(state);
            return new ResponseEntity<>(towns, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/calculateShipping")
    public ResponseEntity<Integer> calculateShipping(@RequestParam("state") String state,
                                                     @RequestParam("town") String town){
        try{
            Integer cost = shipmentService.calculateShipping(state,town);
            return new ResponseEntity<>(cost, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(0, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/order")
    public ResponseEntity<String> submitOrder(@ModelAttribute OrderRequest orderDetails) {
        System.out.println("hello from order");
        System.out.println("orderer firstname " + orderDetails.getFirstname());
        System.out.println("orderer lastname " + orderDetails.getLastname());
        System.out.println("orderer email " + orderDetails.getEmail());
        System.out.println("orderer phone " + orderDetails.getPhone_number());
        System.out.println("orderer to ship " + orderDetails.isToShip());
        System.out.println("orderer to town " + orderDetails.getTown());
        System.out.println("orderer to state " + orderDetails.getState());
        return new ResponseEntity<>("Order submitted successfully", HttpStatus.OK);
    }
}