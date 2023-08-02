package com.profi_shop.controllers.adminControllers;

import com.profi_shop.dto.StoreDTO;
import com.profi_shop.model.Store;
import com.profi_shop.services.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class StoreController {
    private final StoreService storeService;

    @Autowired
    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @PostMapping("/create-store")
    public String createStore(@ModelAttribute StoreDTO storeDTO) {
        try {
            storeService.createStore(storeDTO);
            return "admin/store/createStore";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    @GetMapping("/storeDetails")
    public String storeDetails(@RequestParam("storeId") Long storeId,
                               Model model) {
        Store store = storeService.getStoreById(storeId);
        model.addAttribute("store", store);
        return "admin/store/storeDetails";
    }
    @GetMapping("/stores")
    public String stores(@RequestParam("page")Optional<Integer> page,
                         Model model) {
        Page<Store> stores = storeService.getPagedStores(page.orElse(0),8);
        model.addAttribute("stores", stores);
        return "admin/store/stores";
    }

    @GetMapping("/createStore")
    public String createStore() {
        return "admin/store/createStore";
    }
}
