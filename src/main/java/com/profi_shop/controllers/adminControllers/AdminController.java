package com.profi_shop.controllers.adminControllers;


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



    @GetMapping("/dashboard")
    public String admin() {
        return "admin/dashboard";
    }
}
