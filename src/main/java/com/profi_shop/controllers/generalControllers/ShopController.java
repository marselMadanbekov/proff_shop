package com.profi_shop.controllers.generalControllers;

import com.profi_shop.dto.ProductDetailsDTO;
import com.profi_shop.model.Category;
import com.profi_shop.model.Product;
import com.profi_shop.model.Review;
import com.profi_shop.model.requests.ReviewRequest;
import com.profi_shop.services.*;
import com.profi_shop.services.facade.ProductFacade;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/shop")
public class ShopController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final ProductGroupService productGroupService;
    private final ProductFacade productFacade;
    private final ReviewService reviewService;

    public ShopController(ProductService productService, CategoryService categoryService, ProductGroupService productGroupService, ProductFacade productFacade, ReviewService reviewService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.productGroupService = productGroupService;
        this.productFacade = productFacade;
        this.reviewService = reviewService;
    }

    @GetMapping("")
    public String shopPage(@RequestParam(value = "categoryId", required = false) Optional<Long> categoryId,
                           @RequestParam(value = "size",required = false) Optional<String> size,
                           @RequestParam(value = "color",required = false) Optional<String> color,
                           @RequestParam(value = "minPrice",required = false) Optional<Integer> minPrice,
                           @RequestParam(value = "maxPrice",required = false) Optional<Integer> maxPrice,
                           @RequestParam(value = "query", required = false) Optional<String> query,
                           @RequestParam(value = "page", required = false) Optional<Integer> page,
                           @RequestParam(value = "sort", required = false) Optional<Integer> sort,
                           Model model){

        Page<ProductDetailsDTO> products = productFacade.mapToProductDetailsDTOPage(productService.productsFilteredPage(page.orElse(0),categoryId.orElse(0L),size.orElse(null),query.orElse(""),minPrice.orElse(0),maxPrice.orElse(0),sort.orElse(0)));
        List<Category> categories = categoryService.getMainCategories();


        model.addAttribute("products",products);
        model.addAttribute("categories",categories);

        model.addAttribute("sortType",sort.orElse(0));
        model.addAttribute("minPrice", minPrice.orElse(0));
        model.addAttribute("maxPrice", maxPrice.orElse(0));
        model.addAttribute("selectedCategoryId", categoryId.orElse(0L));
        model.addAttribute("selectedSize", size.orElse(""));
        model.addAttribute("query", query.orElse(""));

        return "shop/shop";
    }

    @GetMapping("/search")
    public String searchProduct(@RequestParam(value = "categoryId") Long categoryId,
                                @RequestParam(value = "query") String query,
                                Model model){
        System.out.println(query);
        Page<ProductDetailsDTO> products = productFacade.mapToProductDetailsDTOPage(productService.search(query, 0));
        List<Category> categories = categoryService.getAllCategories();


        model.addAttribute("products",products);
        model.addAttribute("categories",categories);

        model.addAttribute("sortType",0);
        model.addAttribute("minPrice", 0);
        model.addAttribute("maxPrice", 0);
        model.addAttribute("selectedCategoryId", 0);
        model.addAttribute("selectedSize", 0);
        model.addAttribute("query", query);
        return "shop/shop";
    }

    @GetMapping("/productDetails")
    public String productDetails(@RequestParam("productId") Long productId,
                                 Principal principal,
                                 Model model){
        ProductDetailsDTO product = productFacade.productToProductDetailsDTO(productService.getProductById(productId));
        List<Product> sameGroup = productGroupService.getListOfProductsByProductId(productId);
        Page<Review> reviews = reviewService.lastReviewsByProductId(productId);
        List<Category> categories = categoryService.getMainCategories();
        model.addAttribute("categories",categories);
        model.addAttribute("group", sameGroup);
        model.addAttribute("reviews", reviews);
        model.addAttribute("authenticated", principal != null);
        model.addAttribute("product", product);
        return "shop/productDetails";
    }

    @PostMapping("/productReview")
    public ResponseEntity<String> productReview(@RequestBody ReviewRequest request,
                                                Principal principal){
        try{
            System.out.println("Review is come " + principal.getName());
            reviewService.createReview(request, principal);
            return new ResponseEntity<>("Отзыв сохранен", HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>( "Error", HttpStatus.BAD_REQUEST);
        }
    }


}
