package com.profi_shop.controllers.generalControllers;

import com.profi_shop.services.CategoryService;
import com.profi_shop.services.ProductService;
import com.profi_shop.services.StockService;
import com.profi_shop.services.facade.ProductFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping
public class MainPageController {
    private final ProductService productService;
    private final ProductFacade productFacade;
    private final CategoryService categoryService;
    private final StockService stockService;

    class SearchRequest{
        public String query;
        public Long categoryId;
    }
    @Autowired
    public MainPageController(ProductService productService, ProductFacade productFacade, CategoryService categoryService, StockService stockService) {
        this.productService = productService;

        this.productFacade = productFacade;
        this.categoryService = categoryService;
        this.stockService = stockService;
    }





}
