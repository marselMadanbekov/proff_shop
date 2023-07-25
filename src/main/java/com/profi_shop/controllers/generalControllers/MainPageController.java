package com.profi_shop.controllers.generalControllers;

import com.profi_shop.dto.ProductDTO;
import com.profi_shop.model.Category;
import com.profi_shop.services.CategoryService;
import com.profi_shop.services.ProductService;
import com.profi_shop.services.facade.ProductFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping
public class MainPageController {
    private final ProductService productService;
    private final ProductFacade productFacade;

    private final CategoryService categoryService;

    @Autowired
    public MainPageController(ProductService productService, ProductFacade productFacade, CategoryService categoryService) {
        this.productService = productService;

        this.productFacade = productFacade;
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public String mainPage(Model model){
        Page<ProductDTO> products = productFacade.mapToProductDTOPage(productService.getPagedProducts(0, 10));
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories",categories);
        model.addAttribute("products", products);
        return "shop/mainPage";
    }

    @GetMapping("/productDetails")
    public String productDetails(@RequestParam("productId") Long productId,
                                 Model model){
        ProductDTO product = productFacade.productToProductDTO(productService.getProductById(productId));
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories",categories);
        model.addAttribute("product", product);
        return "shop/proff_productDetails";
    }
}
