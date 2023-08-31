package com.profi_shop.controllers.adminControllers;

import com.profi_shop.dto.ProductQuantityDTO;
import com.profi_shop.dto.StoreAnalyticsDTO;
import com.profi_shop.dto.StoreDTO;
import com.profi_shop.model.Order;
import com.profi_shop.model.Store;
import com.profi_shop.services.StoreAnalyticsService;
import com.profi_shop.services.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class StoreController {
    private final StoreService storeService;
    private final StoreAnalyticsService analyticsService;

    @Autowired
    public StoreController(StoreService storeService, StoreAnalyticsService analyticsService) {
        this.storeService = storeService;
        this.analyticsService = analyticsService;
    }

    @PostMapping("/create-store")
    public ResponseEntity<Map<String,String>> createStore(@ModelAttribute StoreDTO storeDTO) {
        Map<String,String> response = new HashMap<>();
        try {
            storeService.createStore(storeDTO);
            response.put("message","Магазин успешно создан");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/storeDetails")
    public String storeDetails(@RequestParam("storeId") Long storeId,
                               @RequestParam(value = "dateFrom",required = false) Optional<Date> startDate,
                               @RequestParam(value = "dateTo",required = false) Optional<Date> endDate,
                               @RequestParam(value = "page",required = false) Optional<Integer> page,
                               @RequestParam(value = "sort",required = false) Optional<Integer> sort,
                               Model model) {
        Store store = storeService.getStoreById(storeId);

        StoreAnalyticsDTO storeInfo = analyticsService.getStoreAnalytics(storeId,
                startDate.orElse(Date.valueOf(LocalDate.now().minusDays(30))),
                endDate.orElse(Date.valueOf(LocalDate.now())));


        Page<ProductQuantityDTO> products = analyticsService.getProductAndQuantityPage(storeId,page.orElse(0),sort.orElse(0));

        model.addAttribute("products", products);
        model.addAttribute("storeInfo", storeInfo);
        model.addAttribute("selectedSort", sort.orElse(0));
        model.addAttribute("startDate", startDate.orElse(Date.valueOf(LocalDate.now().minusDays(30))));
        model.addAttribute("endDate", endDate.orElse(Date.valueOf(LocalDate.now())));
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
