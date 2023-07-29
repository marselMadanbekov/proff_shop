package com.profi_shop.controllers.adminControllers;

import com.profi_shop.model.Category;
import com.profi_shop.model.Product;
import com.profi_shop.model.requests.ProductCreateRequest;
import com.profi_shop.model.Store;
import com.profi_shop.services.CategoryService;
import com.profi_shop.services.ProductService;
import com.profi_shop.services.StoreHouseService;
import com.profi_shop.services.StoreService;
import com.profi_shop.settings.Templates;
import org.springframework.cache.annotation.CacheEvict;
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
    private final ProductService productService;
    private final CategoryService categoryService;

    private final StoreHouseService stockService;
    private final StoreService storeService;

    public ProductController(ProductService productService, CategoryService categoryService, StoreHouseService stockService, StoreService storeService) {
        this.productService = productService;
        this.categoryService = categoryService;
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

    @CacheEvict
    @GetMapping("/products")
    public String products(@RequestParam(value = "page", required = false) Optional<Integer> page,
                           Model model) {
        List<Store> stores = storeService.getAllStores();
        Page<Product> products = productService.getPagedProducts(page.orElse(0), 8);

        for (Product product : products) {
            System.out.println(product.getName() + product.getPrice());
        }
        model.addAttribute("products", products);
        model.addAttribute("stores", stores);
        model.addAttribute("uploadDir", Templates.uploadDir);
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
            return "redirect:/admin/productDetails?productId="+product.getId();
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping("/deletePhoto")
    public String deletePhoto(@RequestParam("photo") String photo,
                              @RequestParam("productId") Long productId){
        try {
            productService.deletePhotoByProductId(photo,productId);
            return "redirect:/productDetails?productId="+productId;
        }catch (Exception e){
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

    @PostMapping("/products/search")
    public ResponseEntity<Page<Product>> searchProduct(@RequestParam(value = "page",required = false) Optional<Integer> page,
                                                       @RequestBody String request) {
        Page<Product> products = productService.search(request,page.orElse(0));
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/productsByPage")
    public ResponseEntity<Page<Product>> productPage(@RequestParam("page") Integer page) {
        Page<Product> products = productService.getPagedProducts(page, 10);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
