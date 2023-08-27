package com.profi_shop.controllers.adminControllers;

import com.profi_shop.model.Consumption;
import com.profi_shop.model.Store;
import com.profi_shop.services.ConsumptionService;
import com.profi_shop.services.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class ConsumptionsController {

    private final ConsumptionService consumptionService;

    private final StoreService storeService;

    @Autowired
    public ConsumptionsController(ConsumptionService consumptionService, StoreService storeService) {
        this.consumptionService = consumptionService;
        this.storeService = storeService;
    }

    @GetMapping("/consumptions")
    public String consumptions(@RequestParam("storeId") Long storeId,
                               @RequestParam(value = "page", required = false) Optional<Integer> page,
                               @RequestParam(value = "sort", required = false) Optional<Integer> sort,
                               Model model){
        Store store = storeService.getStoreById(storeId);
        Page<Consumption> consumptions = consumptionService.getPagedConsumptions(storeId,page.orElse(0), sort.orElse(0));
        model.addAttribute("store", store);
        model.addAttribute("consumptions", consumptions);
        model.addAttribute("selectedSort", sort.orElse(0));

        return "admin/consumptions/consumptions";
    }

    @PostMapping("/consumptions/create")
    public ResponseEntity<Map<String,String>> createConsumption(@RequestParam Long storeId,
                                                                @RequestParam Integer amount,
                                                                @RequestParam String description,
                                                                Principal principal){
        Map<String, String> response = new HashMap<>();
        try{
            consumptionService.createConsumptionByStore(storeId,amount,description, principal.getName());
            response.put("message", "Расход успешно создан");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
