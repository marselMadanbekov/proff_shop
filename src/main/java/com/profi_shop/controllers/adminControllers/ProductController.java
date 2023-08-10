package com.profi_shop.controllers.adminControllers;

import com.profi_shop.dto.ProductDTO;
import com.profi_shop.dto.ProductDetailsDTO;
import com.profi_shop.model.Category;
import com.profi_shop.model.Product;
import com.profi_shop.model.requests.ProductCreateRequest;
import com.profi_shop.model.Store;
import com.profi_shop.services.CategoryService;
import com.profi_shop.services.ProductService;
import com.profi_shop.services.StoreHouseService;
import com.profi_shop.services.StoreService;
import com.profi_shop.services.facade.ProductFacade;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class ProductController {
    @Value("${uploads_path}")
    private String uploadDir;
    private final ProductService productService;
    private final CategoryService categoryService;

    private final ProductFacade productFacade;
    private final StoreHouseService stockService;
    private final StoreService storeService;

    public ProductController(ProductService productService, CategoryService categoryService, ProductFacade productFacade, StoreHouseService stockService, StoreService storeService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.productFacade = productFacade;
        this.stockService = stockService;
        this.storeService = storeService;
    }


    @PostMapping("/create-product")
    public ResponseEntity<String> createProduct(@ModelAttribute ProductCreateRequest product) {
        try {
            Product createdProduct = productService.createProduct(product);
            stockService.createStocksProduct(createdProduct);
            return new ResponseEntity<>("Successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/products")
    public String products(@RequestParam(value = "categoryId", required = false) Optional<Long> categoryId,
                           @RequestParam(value = "size", required = false) Optional<Integer> size,
                           @RequestParam(value = "minPrice", required = false) Optional<Integer> minPrice,
                           @RequestParam(value = "maxPrice", required = false) Optional<Integer> maxPrice,
                           @RequestParam(value = "query", required = false) Optional<String> query,
                           @RequestParam(value = "page", required = false) Optional<Integer> page,
                           @RequestParam(value = "sort", required = false) Optional<Integer> sort,
                           Model model) {
        List<Store> stores = storeService.getAllStores();
        List<Category> categories = categoryService.getAllCategories();
        Page<ProductDetailsDTO> products = productFacade.mapToProductDetailsDTOPage(productService.productsFilteredPage(page.orElse(0), categoryId.orElse(0L), size.orElse(0), query.orElse(""), minPrice.orElse(0), maxPrice.orElse(0), sort.orElse(0)));


        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("stores", stores);

        model.addAttribute("sortType", sort.orElse(0));
        model.addAttribute("minPrice", minPrice.orElse(0));
        model.addAttribute("maxPrice", maxPrice.orElse(0));
        model.addAttribute("selectedCategory", categoryId.orElse(0L));
        model.addAttribute("selectedSize", size.orElse(0));
        model.addAttribute("query", query.orElse(""));
        return "admin/product/products";
    }

    @PostMapping("/receive-product")
    public String receiveProduct(@RequestParam("store") Long storeId,
                                 @RequestParam("productId") Long productId,
                                 @RequestParam("quantity") int quantity) {
        try {

            stockService.quantityUp(storeId, productId, quantity);
            return "admin/product/products";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "admin/product/products";
        }
    }

    @PostMapping("/addPhoto")
    public String addPhoto(@RequestParam("photo") MultipartFile photo,
                           @RequestParam("productId") Long productId) {
        try {
            Product product = productService.addPhotoToProductById(productId, photo);
            return "redirect:/admin/productDetails?productId=" + product.getId();
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping("/deletePhoto")
    public String deletePhoto(@RequestParam("photo") String photo,
                              @RequestParam("productId") Long productId) {
        try {
            productService.deletePhotoByProductId(photo, productId);
            return "redirect:/productDetails?productId=" + productId;
        } catch (Exception e) {
            return e.getMessage();
        }

    }

    @GetMapping("/productDetails")
    public String productDetails(@RequestParam("productId") Long productId, Model model) {
        Product product = productService.getProductById(productId);
        model.addAttribute("product", product);
        return "admin/product/productDetails";
    }

    @GetMapping("/createProduct")
    public String createProduct(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "admin/product/createProduct";
    }

    @GetMapping("/products/search")
    public ResponseEntity<Page<Product>> searchProduct(@RequestParam(value = "page", required = false) Optional<Integer> page,
                                                       @RequestParam("query") String query) {
        Page<Product> products = productService.search(query, page.orElse(0));
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/productsByPage")
    public ResponseEntity<Page<Product>> productPage(@RequestParam("page") Integer page) {
        Page<Product> products = productService.getPagedProducts(page, 10);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<Object> filterProducts(@RequestParam(value = "size", required = false) Optional<Integer> size,
                                                  @RequestParam(value = "minPrice", required = false) Optional<Integer> minPrice,
                                                  @RequestParam(value = "maxPrice", required = false) Optional<Integer> maxPrice,
                                                  @RequestParam(value = "query", required = false) Optional<String> query,
                                                  @RequestParam(value = "page", required = false) Optional<Integer> page,
                                                  @RequestParam(value = "sort", required = false) Optional<Integer> sort) {
        try {

            Page<ProductDTO> products = productFacade.mapToProductDTOPage(productService.productsFilteredPage(page.orElse(0), 0L, size.orElse(0), query.orElse(""), minPrice.orElse(0), maxPrice.orElse(0), sort.orElse(0)));
            return new ResponseEntity<>(products, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("ERROR", HttpStatus.BAD_REQUEST);
        }
    }
}
