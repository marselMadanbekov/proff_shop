package com.profi_shop.controllers.generalControllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/error1")
public class ErrorController {

    @GetMapping("/error")
    public String error(@RequestParam(value = "message",required = false) Optional<String> message,
                        Model model){
        model.addAttribute("message",message.orElse("NOT FOUND EXCEPTION"));
        return "shop/error";
    }
}
