package com.profi_shop.model.requests;

import lombok.Data;

@Data
public class ReviewRequest {
    private String review;
    private int rating;
    private Long productId;
}
