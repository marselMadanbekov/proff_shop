package com.profi_shop.controllers.adminControllers;

import com.profi_shop.model.Order;
import com.profi_shop.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
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
}
