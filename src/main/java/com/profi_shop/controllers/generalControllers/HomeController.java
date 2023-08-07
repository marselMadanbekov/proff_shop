package com.profi_shop.controllers.generalControllers;

import com.profi_shop.dto.ProductDTO;
import com.profi_shop.dto.StockDTO;
import com.profi_shop.model.Category;
import com.profi_shop.model.Stock;
import com.profi_shop.services.CategoryService;
import com.profi_shop.services.ProductService;
import com.profi_shop.services.StockService;
import com.profi_shop.services.facade.ProductFacade;
import com.profi_shop.services.facade.StockFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {
    private final ProductService productService;
    private final ProductFacade productFacade;
    private final StockService stockService;
    private final CategoryService categoryService;

    private final StockFacade stockFacade;

    @Autowired
    public HomeController(ProductService productService, ProductFacade productFacade, StockService stockService, CategoryService categoryService, StockFacade stockFacade) {
        this.productService = productService;
        this.productFacade = productFacade;
        this.stockService = stockService;
        this.categoryService = categoryService;
        this.stockFacade = stockFacade;
    }

    @GetMapping("")
    public String mainPage(Model model){
        Page<ProductDTO> products = productFacade.mapToProductDTOPage(productService.getPagedProducts(0, 10));
        List<Category> categories = categoryService.getMainCategories();
        StockDTO todayDeals = stockFacade.stockToStockDTO(stockService.getTodayDeals());

        model.addAttribute("categories",categories);
        model.addAttribute("products", products);
        model.addAttribute("todayDeals", todayDeals);
        return "shop/mainPage";
    }
}
