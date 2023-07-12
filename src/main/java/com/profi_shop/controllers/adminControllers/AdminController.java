package com.profi_shop.controllers;


import com.profi_shop.dto.StoreDTO;
import com.profi_shop.services.PhotoService;
import com.profi_shop.services.ProductService;
import com.profi_shop.services.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final ProductService productService;
    private final PhotoService photoService;

    private final StoreService storeService;

    @Autowired
    public AdminController(ProductService productService, PhotoService photoService, StoreService storeService) {
        this.productService = productService;
        this.photoService = photoService;
        this.storeService = storeService;
    }

    @PostMapping("/create-product")
    public String createProduct(@RequestParam("name") String name,
                                @RequestParam("sku") String sku,
                                @RequestParam("price") String price,
                                @RequestParam("category") String category,
                                @RequestParam("photo") MultipartFile photo,
                                @RequestParam("description") String description) {
        try {
            System.out.println("hello world " + name);
            photoService.savePhoto(photo);
            return "shop/index";
        } catch (Exception e) {
            return "redirect:/"; // Перенаправьте пользователя на нужную страницу после успешного создания продукта
        }

    }

    @PostMapping("/create-store")
    public String createStore(@RequestParam("name") String name,
                              @RequestParam("town") String town,
                              @RequestParam("address") String address,
                              @RequestParam("phone_number") String phone_number) {
        try {
            StoreDTO storeDTO = new StoreDTO();
            storeDTO.setTown(town);
            storeDTO.setName(name);
            storeDTO.setPhone_number(phone_number);
            storeService.createStore(storeDTO);
            return "shop/index";
        } catch (Exception e) {
            return "redirect:/"; // Перенаправьте пользователя на нужную страницу после успешного создания продукта
        }

    }

    @PostMapping("/create-category")
    public String createCategory(@RequestParam("name") String name) {
        try {
            StoreDTO storeDTO = new StoreDTO();
            storeDTO.setTown(town);
            storeDTO.setName(name);
            storeDTO.setPhone_number(phone_number);
            storeService.createStore(storeDTO);
            return "shop/index";
        } catch (Exception e) {
            return "redirect:/"; // Перенаправьте пользователя на нужную страницу после успешного создания продукта
        }

    }

    @GetMapping("/dashboard")
    public String admin() {
        return "admin/dashboard";
    }

    @GetMapping("/orders")
    public String orders() {
        return "admin/orders";
    }

    @GetMapping("/products")
    public String products() {
        return "admin/products";
    }

    @GetMapping("/categories")
    public String categories() {
        return "admin/categories";
    }

    @GetMapping("/storeDetails")
    public String storeDetails() {
        return "admin/storeDetails";
    }

    @GetMapping("/stores")
    public String stores() {
        return "admin/stores";
    }

    @GetMapping("/productDetails")
    public String productDetails() {
        return "admin/productDetails";
    }

    @GetMapping("/createStore")
    public String createStore() {
        return "admin/createStore";
    }

    @GetMapping("/createProduct")
    public String createProduct() {
        return "admin/createProduct";
    }
}
