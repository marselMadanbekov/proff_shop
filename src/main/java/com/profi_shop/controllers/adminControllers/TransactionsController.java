package com.profi_shop.controllers.adminControllers;

import com.profi_shop.model.Store;
import com.profi_shop.model.Transaction;
import com.profi_shop.services.StoreService;
import com.profi_shop.services.TransactionService;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class TransactionsController {
    private final TransactionService transactionService;
    private final StoreService storeService;

    @Autowired
    public TransactionsController(TransactionService transactionService, StoreService storeService) {
        this.transactionService = transactionService;
        this.storeService = storeService;
    }

    @GetMapping("/transactions")
    public String transactions(@RequestParam("storeId") Long storeId,
                               @RequestParam(value = "sendPage", required = false) Optional<Integer> page,
                               @RequestParam(value = "recPage", required = false) Optional<Integer> recPage,
                               @RequestParam(value = "sendSort", required = false) Optional<Integer> sort,
                               @RequestParam(value = "recSort", required = false) Optional<Integer> recSort,
                               Model model){
        Store store = storeService.getStoreById(storeId);
        Page<Transaction> sendTransactions = transactionService.getPagedSendTransactionsByStore(storeId,page.orElse(0), sort.orElse(0));
        Page<Transaction> receivedTransactions = transactionService.getPagedReceivedTransactionsByStore(storeId,recPage.orElse(0),recSort.orElse(0));
        List<Store> stores = storeService.getAllStores();
        model.addAttribute("store", store);
        model.addAttribute("stores", stores);
        model.addAttribute("sendTransactions", sendTransactions);
        model.addAttribute("receivedTransactions", receivedTransactions);
        model.addAttribute("selectedSort", sort.orElse(0));
        model.addAttribute("recSort", recSort.orElse(0));

        return "admin/transactions/transactions";
    }

    @PostMapping("/transactions/create")
    public ResponseEntity<Map<String,String>> createTransaction(@RequestParam Long senderStoreId,
                                                                @RequestParam Long targetStoreId,
                                                                @RequestParam Integer amount,
                                                                Principal principal){
        Map<String,String> response = new HashMap<>();
        try{
            transactionService.createTransaction(senderStoreId,targetStoreId,principal.getName(),amount);
            response.put("message", "Транзакция успешно создана");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/transactions/allow")
    public ResponseEntity<Map<String,String>> allowTransactions(@RequestParam Long transactionId,
                                                                Principal principal){
        Map<String,String> response = new HashMap<>();
        try{
            transactionService.allowTransaction(transactionId,principal.getName());
            response.put("message", "Транзакция успешно одобрена");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
