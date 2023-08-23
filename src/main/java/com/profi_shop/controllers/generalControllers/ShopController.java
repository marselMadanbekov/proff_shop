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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
                           @RequestParam(value = "brand",required = false) Optional<String> brand,
                           @RequestParam(value = "tag",required = false) Optional<String> tag,
                           @RequestParam(value = "minPrice",required = false) Optional<Integer> minPrice,
                           @RequestParam(value = "maxPrice",required = false) Optional<Integer> maxPrice,
                           @RequestParam(value = "query", required = false) Optional<String> query,
                           @RequestParam(value = "page", required = false) Optional<Integer> page,
                           @RequestParam(value = "sort", required = false) Optional<Integer> sort,
                           Model model){

        Page<ProductDetailsDTO> products = productFacade.mapToProductDetailsDTOPage(
                productService.productsFilteredPage(page.orElse(0),
                        categoryId.orElse(0L),
                        size.orElse(null),
                        query.orElse(""),
                        minPrice.orElse(0),
                        maxPrice.orElse(0),
                        sort.orElse(0),
                        tag.orElse(null),
                        brand.orElse(null)));
        List<Category> categories = categoryService.getMainCategories();
        List<String> tags = productService.getTags();
        List<String> sizes = productService.getSizes();
        List<String> brands = productService.getBrands();


        model.addAttribute("products",products);
        model.addAttribute("categories",categories);
        model.addAttribute("tags", tags);
        model.addAttribute("brands", brands);
        model.addAttribute("sizes", sizes);

        model.addAttribute("sortType",sort.orElse(0));
        model.addAttribute("selectedTag", tag.orElse(null));
        model.addAttribute("selectedBrand", brand.orElse(null));
        model.addAttribute("minPrice", minPrice.orElse(0));
        model.addAttribute("maxPrice", maxPrice.orElse(0));
        model.addAttribute("selectedCategoryId", categoryId.orElse(0L));
        model.addAttribute("selectedSize", size.orElse(null));
        model.addAttribute("query", query.orElse(null));

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
    public ResponseEntity<Map<String,String>> productReview(@RequestBody ReviewRequest request,
                                             Principal principal){
        Map<String,String> response = new HashMap<>();
        try{
            reviewService.createReview(request, principal);
            response.put("message", "Отзыв успешно сохранен");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
