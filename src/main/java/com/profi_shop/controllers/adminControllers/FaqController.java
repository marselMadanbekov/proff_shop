package com.profi_shop.controllers.adminControllers;

import com.profi_shop.model.Faq;
import com.profi_shop.services.FaqService;
import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
@RequestMapping("/admin")
public class FaqController {
    private final FaqService faqService;

    @Autowired
    public FaqController(FaqService faqService) {
        this.faqService = faqService;
    }

    @GetMapping("/faq")
    public String faq(Model model){
        List<Faq> faqs = faqService.getAllFaqs();
        model.addAttribute("faqs", faqs);
        return "admin/faq";
    }

    @PostMapping("/faq/create")
    public ResponseEntity<Map<String,String>> createFaq(@RequestParam String question,
                                                        @RequestParam String answer){
        Map<String,String> response = new HashMap<>();
        try{
            faqService.createFaq(question, answer);
            response.put("message", "Часто задаваемый вопрос успешно создан");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/faq/delete")
    public ResponseEntity<Map<String,String>> deleteFaq(@RequestParam Long faqId){
        Map<String,String> response = new HashMap<>();
        try{
            faqService.deleteFaqById(faqId);
            response.put("message", "Вопрос успешно удален");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
