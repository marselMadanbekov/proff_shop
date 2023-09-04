package com.profi_shop.controllers.adminControllers;

import com.profi_shop.model.Review;
import com.profi_shop.services.ReviewService;
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
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/reviews")
    public String reviews(@RequestParam(value = "sort", required = false) Optional<Integer> sort,
                          @RequestParam(value = "page", required = false) Optional<Integer> page,
                          Model model) {
        Page<Review> reviews = reviewService.getPagedReviews(sort.orElse(0),page.orElse(0));
        model.addAttribute("reviews", reviews);
        model.addAttribute("sort", sort.orElse(0));
        return "admin/reviews";
    }

    @PostMapping("/delete-review")
    public ResponseEntity<Map<String,String>> deleteReview(@RequestParam Long reviewId){
        Map<String,String> response = new HashMap<>();
        try{
            reviewService.deleteReview(reviewId);
            response.put("message", "Успешно удалено");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
