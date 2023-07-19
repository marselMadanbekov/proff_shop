package com.profi_shop.controllers.adminControllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class OrderController {

    @GetMapping("/orders")
    public String orders() {
        return "admin/order/orders";
    }
}
