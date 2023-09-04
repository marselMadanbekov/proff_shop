package com.profi_shop.services;

import com.profi_shop.exceptions.SearchException;
import com.profi_shop.model.Product;
import com.profi_shop.model.Review;
import com.profi_shop.model.User;
import com.profi_shop.model.requests.ReviewRequest;
import com.profi_shop.repositories.ProductRepository;
import com.profi_shop.repositories.ReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    public ReviewService(ReviewRepository reviewRepository, ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
    }

    public void createReview(ReviewRequest request, Principal principal) {
        Review review = new Review();
        Product product = getProductById(request.getProductId());
        review.setText(request.getReview());
        review.setUsername(principal.getName());
        review.setMark(request.getRating());
        review.setProduct(product);
        reviewRepository.save(review);
    }

    public Page<Review> lastReviewsByProductId(Long productId){
        Pageable pageable = PageRequest.of(0,9);
        return reviewRepository.getReviewByProduct(getProductById(productId),pageable);
    }

    public Page<Review> getPageReviewsByUserAndPage(String username, int page){
        Pageable pageable = PageRequest.of(page,10, Sort.by(Sort.Direction.DESC,"date"));
        return reviewRepository.findByUsername(username, pageable);
    }

    public int getAverageReviewByProduct(Product product){
        return (int) Math.ceil(reviewRepository.findAverageMarkByProduct(product).orElse(5.0));
    }

    private Product getProductById(Long productId){
        return productRepository.findById(productId).orElseThrow(() -> new SearchException(SearchException.PRODUCT_NOT_FOUND));
    }

    public Page<Review> getPagedReviews(Integer sort, Integer page) {
        Pageable pageable = switch (sort){
            case 0 -> PageRequest.of(page,15,Sort.by(Sort.Direction.ASC, "mark"));
            case 1 -> PageRequest.of(page, 15, Sort.by(Sort.Direction.DESC, "mark"));
            case 2 -> PageRequest.of(page, 15,Sort.by(Sort.Direction.DESC,"date"));
            default -> PageRequest.of(page, 15, Sort.by(Sort.Direction.ASC, "date"));
        };

        return reviewRepository.findAll(pageable);
    }

    public void deleteReview(Long reviewId) {
        Review review = getReviewById(reviewId);
        reviewRepository.delete(review);
    }

    private Review getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(() -> new SearchException(SearchException.REVIEW_NOT_FOUND));
    }
}
