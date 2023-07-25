package com.profi_shop.services;

import com.profi_shop.exceptions.SearchException;
import com.profi_shop.model.Product;
import com.profi_shop.model.Review;
import com.profi_shop.model.requests.ReviewRequest;
import com.profi_shop.repositories.ProductRepository;
import com.profi_shop.repositories.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    public ReviewService(ReviewRepository reviewRepository, ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
    }

    public void createReview(ReviewRequest request){
        Review review = new Review();
        Product product = getProductById(request.getProductId());
        review.setText(request.getText());
        review.setUserEmail(request.getUserEmail());
        review.setMark(request.getMark());
        review.setProduct(product);
        reviewRepository.save(review);
    }

    public int getAverageReviewByProduct(Product product){
        return (int) Math.ceil(reviewRepository.findAverageMarkByProduct(product).orElse(0.0));
    }

    private Product getProductById(Long productId){
        return productRepository.findById(productId).orElseThrow(() -> new SearchException(SearchException.PRODUCT_NOT_FOUND));
    }
}
