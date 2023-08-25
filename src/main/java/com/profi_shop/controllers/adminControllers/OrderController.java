package com.profi_shop.controllers.adminControllers;

import com.profi_shop.dto.OrderDetailsDTO;
import com.profi_shop.model.Category;
import com.profi_shop.model.Order;
import com.profi_shop.model.Product;
import com.profi_shop.model.requests.OrderUpdateRequest;
import com.profi_shop.services.CategoryService;
import com.profi_shop.services.OrderService;
import com.profi_shop.services.ProductService;
import com.profi_shop.services.ShipmentService;
import com.profi_shop.services.facade.OrderFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
@RequestMapping("/admin")
public class OrderController {

    private final OrderService orderService;
    private final OrderFacade orderFacade;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final ShipmentService shipmentService;

    @Autowired
    public OrderController(OrderService orderService, OrderFacade orderFacade, ProductService productService, CategoryService categoryService, ShipmentService shipmentService) {
        this.orderService = orderService;
        this.orderFacade = orderFacade;
        this.productService = productService;
        this.categoryService = categoryService;
        this.shipmentService = shipmentService;
    }

    @GetMapping("/orders")
    public String orders(@RequestParam(value = "status", required = false)Optional<Integer> status,
                         @RequestParam(value = "sort", required = false) Optional<Integer> sort,
                         @RequestParam(value = "page", required = false) Optional<Integer> page,
                         Model model) {
        Page<Order> orders = orderService.getFilteredOrders(status.orElse(0),sort.orElse(0), page.orElse(0));

        model.addAttribute("orders", orders);
        model.addAttribute("selectedStatus", status.orElse(0));
        model.addAttribute("selectedSort", sort.orElse(0));
        return "admin/order/orders";
    }
    @GetMapping("/orders/createOrder")
    public ResponseEntity<Map<String,String>> createNewOrder(Principal principal){
        Map<String,String> response = new HashMap<>();
        try{
            Long orderId = orderService.createOrderByAdminUsername(principal.getName());
            response.put("message",orderId.toString());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/order/addToOrder")
    public ResponseEntity<Map<String,String>> addToOrder(@RequestParam Long orderId,
                                                         @RequestParam Long productId){
        Map<String,String> response = new HashMap<>();
        try{
            orderService.addProductToOrder(orderId, productId);
            response.put("message", "Продукт успешно добавлен");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/orderDetails")
    public String orderDetails(@RequestParam("orderId") Long orderId,
                               Model model){
        OrderDetailsDTO order = orderFacade.orderToOrderDetails(orderService.getOrderById(orderId));
        List<String> states = shipmentService.getUniqueStates();
        List<String> towns = shipmentService.getUniqueTowns();
        Page<Product> products = productService.getPagedProducts(0,10);
        List<Category> categories = categoryService.getAllCategories();

        model.addAttribute("states", states);
        model.addAttribute("categories", categories);
        model.addAttribute("products", products);
        model.addAttribute("towns", towns);
        model.addAttribute("order", order);
        return "admin/order/orderDetails";
    }

    @GetMapping("/order/removeShipment")
    public ResponseEntity<Map<String,String>> removeShipment(@RequestParam("orderId") Long orderId){
        Map<String,String> response = new HashMap<>();
        try{
            orderService.removeShipmentByOrderId(orderId);
            response.put("message", "Доставка успешно удалена!");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e) {
            System.out.println(e.getMessage());
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/order/itemCountDown")
    public ResponseEntity<Map<String, String>> itemCountDown(@RequestParam Long orderItemId){
        Map<String,String> response = new HashMap<>();
        try{
            orderService.itemQuantityDown(orderItemId);
            response.put("message", "Количество успешно уменьшено");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/order/itemCountUp")
    public ResponseEntity<Map<String, String>> itemCountUp(@RequestParam Long orderItemId){
        Map<String,String> response = new HashMap<>();
        try{
            orderService.itemQuantityUp(orderItemId);
            response.put("message", "Количество успешно увеличено");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/order/statusUp")
    public ResponseEntity<Map<String,String>> statusUp(@RequestParam Long orderId){
        Map<String,String> response = new HashMap<>();
        try{
            orderService.orderStatusUp(orderId);
            response.put("message", "Статус успешно изменен");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/order/statusDown")
    public ResponseEntity<Map<String,String>> statusDown(@RequestParam Long orderId){
        Map<String,String> response = new HashMap<>();
        try{
            orderService.orderStatusDown(orderId);
            response.put("message", "Статус успешно изменен");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/order/removeItem")
    public ResponseEntity<Map<String,String>> removeItemFromOrder(@RequestParam("orderItemId") Long orderItemId){
        Map<String, String> response = new HashMap<>();
        try{
            orderService.deleteOrderItem(orderItemId);
            response.put("message", "Товар успешно удален из заказа!");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/order/update-order")
    public ResponseEntity<Map<String,String>> updateOrder(@RequestBody List<OrderUpdateRequest> orderItems){
        Map<String,String> response = new HashMap<>();
        try{
            orderService.updateOrderItemSizes(orderItems);
            response.put("message", "Детали заказа успешно обновлены");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/order/newShipment")
    public ResponseEntity<Map<String,String>> newShipmentToOrder(@RequestParam Long orderId,
                                                                 @RequestParam String state,
                                                                 @RequestParam String town){
        Map<String,String> response = new HashMap<>();
        try{
            orderService.newShipmentToOrder(orderId,state,town);
            response.put("message", "Доставка успешно сохранена");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e.getMessage());
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
