package com.profi_shop.controllers.adminControllers;

import com.profi_shop.model.Category;
import com.profi_shop.model.Product;
import com.profi_shop.model.requests.StockRequest;
import com.profi_shop.services.CategoryService;
import com.profi_shop.services.ProductService;
import com.profi_shop.services.StockService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class StocksController {

    private final CategoryService categoryService;
    private final ProductService productService;

    private final StockService stockService;

    public StocksController(CategoryService categoryService, ProductService productService, StockService stockService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.stockService = stockService;
    }

    @GetMapping("/stocks")
    public String stocks() {
        return "admin/stock/stocksPage";
    }

    @GetMapping("/createStock")
    public String createStock(@RequestParam("productPage") Optional<Integer> productPage,
                              @RequestParam("categoryPage") Optional<Integer> categoryPage,
                              Model model) {
        Page<Product> products = productService.getPagedProducts(productPage.orElse(0), 10);
        Page<Category> categories = categoryService.getPagedCategories(categoryPage.orElse(0), 10);
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        return "admin/stock/createStock";
    }

    @PostMapping("/create-stock")
    public ResponseEntity<String> createStock(@RequestBody StockRequest createStock) {
        try {
            System.out.println(createStock.getType());
            for(Long id : createStock.getParticipants().keySet()){
                System.out.println(id);
            }
            stockService.createStock(createStock);
            return new ResponseEntity<>("successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }
}
