package com.profi_shop.controllers.generalControllers;

import com.profi_shop.dto.ProductDTO;
import com.profi_shop.dto.ProductDetailsDTO;
import com.profi_shop.dto.StockDTO;
import com.profi_shop.model.Category;
import com.profi_shop.model.MainPage;
import com.profi_shop.model.MainStore;
import com.profi_shop.services.*;
import com.profi_shop.services.facade.ProductFacade;
import com.profi_shop.services.facade.StockFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/home")
public class HomeController {
    private final ProductService productService;
    private final ProductFacade productFacade;
    private final StockService stockService;
    private final CategoryService categoryService;
    private final MainStoreService mainStoreService;
    private final StockFacade stockFacade;
    private final MainPageService mainPageService;

    @Autowired
    public HomeController(ProductService productService, ProductFacade productFacade, StockService stockService, CategoryService categoryService, MainStoreService mainStoreService, StockFacade stockFacade, MainPageService mainPageService) {
        this.productService = productService;
        this.productFacade = productFacade;
        this.stockService = stockService;
        this.categoryService = categoryService;
        this.mainStoreService = mainStoreService;
        this.stockFacade = stockFacade;
        this.mainPageService = mainPageService;
    }

    @GetMapping("")
    public String mainPage(Model model){
        Page<ProductDetailsDTO> products = productFacade.mapToProductDetailsDTOPage(productService.getPagedProducts(0, 10));
        List<Category> categories = categoryService.getMainCategories();
        StockDTO todayDeals = stockFacade.stockToStockDTO(stockService.getTodayDeals());
        MainStore mainStore = mainStoreService.getMainStore();
        MainPage mainPage = mainPageService.getMainPage();

        model.addAttribute("mainPage", mainPage);
        model.addAttribute("mainStore", mainStore);
        model.addAttribute("categories",categories);
        model.addAttribute("products", products);
        model.addAttribute("todayDeals", todayDeals);
        return "shop/mainPage";
    }

    @GetMapping("/stocks")
    public String stocks(@RequestParam(value = "page", required = false)Optional<Integer> page,
                         @RequestParam(value = "sort", required = false) Optional<Integer> sort,
                         Model model){
        Page<ProductDTO> products = productFacade.mapToProductDTOPage(productService.productsByActiveStocks(page.orElse(0),sort.orElse(0)));
        List<Category> categories = categoryService.getMainCategories();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        return "shop/stocks";
    }
}
