package com.profi_shop.controllers.adminControllers;

import com.profi_shop.model.MainPage;
import com.profi_shop.services.MainPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class MainPageEditor {

    private final MainPageService mainPageService;

    @Autowired
    public MainPageEditor(MainPageService mainPageService) {
        this.mainPageService = mainPageService;
    }

    @GetMapping("/mainPageEditor")
    public String mainPageEditor(Model model){
        MainPage mainPage = mainPageService.getMainPage();
        model.addAttribute("mainPage",mainPage);
        return "admin/mainPageEditor/mainPageEditor";
    }

    @PostMapping("/carousel-first-item")
    public ResponseEntity<Map<String,String>> firstItem(
            @RequestParam(value = "photo", required = false) MultipartFile photo,
            @RequestParam(value = "firstPhotoText", required = false) String firstPhotoText) {
        Map<String,String> response = new HashMap<>();
        try{
            mainPageService.setFirstItemOnSlider(photo,firstPhotoText);
            response.put("message", "Изменения успешно сохранены");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/carousel-second-item")
    public ResponseEntity<Map<String,String>> secondItem(
            @RequestParam(value = "photo", required = false) MultipartFile photo,
            @RequestParam(value = "secondPhotoText", required = false) String secondPhotoText) {
        Map<String,String> response = new HashMap<>();
        try{
            mainPageService.setSecondItemOnSlider(photo,secondPhotoText);
            response.put("message", "Изменения успешно сохранены");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/carousel-third-item")
    public ResponseEntity<Map<String,String>> thirdItem(
            @RequestParam(value = "photo", required = false) MultipartFile photo,
            @RequestParam(value = "thirdPhotoText", required = false) String thirdPhotoText) {
        Map<String,String> response = new HashMap<>();
        try{
            mainPageService.setThirdItemOnSlider(photo,thirdPhotoText);
            response.put("message", "Изменения успешно сохранены");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/sales-item")
    public ResponseEntity<Map<String,String>> salesItem(
            @RequestParam(value = "photo", required = false) MultipartFile photo,
            @RequestParam(value = "salesText", required = false) String salesText) {
        Map<String,String> response = new HashMap<>();
        try{
            mainPageService.setSalesItem(photo,salesText);
            response.put("message", "Изменения успешно сохранены");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/top-item")
    public ResponseEntity<Map<String,String>> topItem(
            @RequestParam(value = "photo", required = false) MultipartFile photo,
            @RequestParam(value = "topText", required = false) String topText) {
        Map<String,String> response = new HashMap<>();
        try{
            mainPageService.setTopProductItem(photo,topText);
            response.put("message", "Изменения успешно сохранены");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/set-delivery-text")
    public ResponseEntity<Map<String,String>> deliveryText(
            @RequestParam(value = "deliveryText") String deliveryText) {
        Map<String,String> response = new HashMap<>();
        try{
            mainPageService.setDeliveryDescription(deliveryText);
            response.put("message", "Изменения успешно сохранены");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/set-payment-text")
    public ResponseEntity<Map<String,String>> paymentText(
            @RequestParam(value = "paymentText") String paymentText) {
        Map<String,String> response = new HashMap<>();
        try{
            mainPageService.setPaymentDescription(paymentText);
            response.put("message", "Изменения успешно сохранены");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/set-service-text")
    public ResponseEntity<Map<String,String>> serviceText(
            @RequestParam(value = "serviceText") String serviceText) {
        Map<String,String> response = new HashMap<>();
        try{
            mainPageService.setServiceDescription(serviceText);
            response.put("message", "Изменения успешно сохранены");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            response.put("error",e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
