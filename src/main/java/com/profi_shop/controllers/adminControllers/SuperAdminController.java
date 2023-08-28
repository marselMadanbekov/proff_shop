package com.profi_shop.controllers.adminControllers;

import com.profi_shop.auth.requests.AdminCreateRequest;
import com.profi_shop.model.MainStore;
import com.profi_shop.model.Store;
import com.profi_shop.model.Transaction;
import com.profi_shop.services.MainStoreService;
import com.profi_shop.services.StoreService;
import com.profi_shop.services.TransactionService;
import com.profi_shop.services.UserService;
import jakarta.validation.Valid;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin/superAdmin")
public class SuperAdminController {

    private final UserService userService;
    private final StoreService storeService;
    private final MainStoreService mainStoreService;
    private final TransactionService transactionService;


    @Autowired
    public SuperAdminController(UserService userService, StoreService storeService, MainStoreService mainStoreService, TransactionService transactionService) {
        this.userService = userService;
        this.storeService = storeService;
        this.mainStoreService = mainStoreService;
        this.transactionService = transactionService;
    }

    @GetMapping("/main-store")
    public String mainStore(@RequestParam(value = "sort",required = false) Optional<Integer> sort,
                            @RequestParam(value = "page",required = false) Optional<Integer> page,
                            Model model){
        MainStore mainStore = mainStoreService.getMainStore();
        Page<Transaction> transactions = transactionService.mainStoreTransactions(sort.orElse(0), page.orElse(0));

        model.addAttribute("sort", sort.orElse(0));
        model.addAttribute("transactions", transactions);
        model.addAttribute("mainStore", mainStore);
        return "admin/store/mainStore";
    }

    @PostMapping("/add-phone")
    public ResponseEntity<Map<String,String>> addPhone(@RequestParam String phone){
        Map<String,String> response = new HashMap<>();
        try{
            mainStoreService.addNewPhoneNumber(phone);
            response.put("message", "Номер успешно добавлен");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/delete-phone")
    public ResponseEntity<Map<String,String>> deletePhone(@RequestParam String phone){
        Map<String,String> response = new HashMap<>();
        try{
            mainStoreService.deletePhone(phone);
            response.put("message", "Номер успешно добавлен");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/set-state")
    public ResponseEntity<Map<String,String>> setState(@RequestParam String state){
        Map<String,String> response = new HashMap<>();
        try{
            mainStoreService.setState(state);
            response.put("message", "Область успешно добавлена");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/set-town")
    public ResponseEntity<Map<String,String>> setTown(@RequestParam String town){
        Map<String,String> response = new HashMap<>();
        try{
            mainStoreService.setTown(town);
            response.put("message", "Город успешно добавлен");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/set-instagram")
    public ResponseEntity<Map<String,String>> setInstagram(@RequestParam String instagram){
        Map<String,String> response = new HashMap<>();
        try{
            mainStoreService.setInstagram(instagram);
            response.put("message", "Инстаграм успешно добавлен");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/set-email")
    public ResponseEntity<Map<String,String>> setEmail(@RequestParam String email){
        Map<String,String> response = new HashMap<>();
        try{
            mainStoreService.setEmail(email);
            response.put("message", "Email успешно добавлен");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/set-workTime")
    public ResponseEntity<Map<String,String>> setWorkTime(@RequestParam String workTime){
        Map<String,String> response = new HashMap<>();
        try{
            mainStoreService.setWorkingTime(workTime);
            response.put("message", "Рабочее время успешно добавлено");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
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
