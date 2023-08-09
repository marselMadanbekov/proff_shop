package com.profi_shop.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.NoHandlerFoundException;

@Controller
@RequestMapping("/error")
public class CustomErrorController implements ErrorController {

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNotFoundError() {
        return "shop/error";
    }
}
