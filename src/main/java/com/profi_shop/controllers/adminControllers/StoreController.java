package com.profi_shop.controllers.adminControllers;

import com.profi_shop.dto.StoreDTO;
import com.profi_shop.model.Store;
import com.profi_shop.services.StoreAnalyticsService;
import com.profi_shop.services.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
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
                               @RequestParam(value = "dateFrom",required = false) Optional<Date> startDate,
                               @RequestParam(value = "dateTo",required = false) Optional<Date> endDate,
                               Model model) {
        Store store = storeService.getStoreById(storeId);
        int onlineSales = analyticsService.sumOfOnlineSalesByStore(storeId,
                startDate.orElse(Date.valueOf(LocalDate.now().minusDays(30))),
                endDate.orElse(Date.valueOf(LocalDate.now())));

        int offlineSales = analyticsService.sumOfOfflineSalesByStore(storeId,
                startDate.orElse(Date.valueOf(LocalDate.now().minusDays(30))),
                endDate.orElse(Date.valueOf(LocalDate.now())));
        int canceledOrders = analyticsService.countOfCanceledOrdersByStore(storeId,
                startDate.orElse(Date.valueOf(LocalDate.now().minusDays(30))),
                endDate.orElse(Date.valueOf(LocalDate.now())));

        model.addAttribute("onlineSales", onlineSales);
        model.addAttribute("offlineSales", offlineSales);
        model.addAttribute("canceledOrders", canceledOrders);
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
