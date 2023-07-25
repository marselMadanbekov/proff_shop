package com.profi_shop.model.requests;

import lombok.Data;

@Data
public class ReviewRequest {
    private String text;
    private String userEmail;
    private int mark;
    private Long productId;
}
