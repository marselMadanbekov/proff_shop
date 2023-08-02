package com.profi_shop.controllers.adminControllers;

import com.profi_shop.auth.requests.AdminCreateRequest;
import com.profi_shop.model.Store;
import com.profi_shop.services.StoreService;
import com.profi_shop.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/superAdmin")
public class SuperAdminController {

    private final UserService userService;
    private final StoreService storeService;

    @Autowired
    public SuperAdminController(UserService userService, StoreService storeService) {
        this.userService = userService;
        this.storeService = storeService;
    }

    @GetMapping("/create-admin")
    public String createAdmin(@RequestParam(value = "storeId")Optional<Long> storeId,
                              Model model){
        List<Store> stores = storeService.getAllStores();
        model.addAttribute("stores", stores);
        model.addAttribute("selectedStore", storeId.orElse(1L));
        return "admin/users/createAdmin";
    }

    @PostMapping("/create-admin")
    public ResponseEntity<String> createAdmin(@Valid AdminCreateRequest adminCreateRequest){
        try{
            userService.createAdmin(adminCreateRequest);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
