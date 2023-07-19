package com.profi_shop.model.requests;

import com.profi_shop.model.Category;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProductCreateRequest {
    private Long id;
    private String name;
    private int prime_cost;
    private int price;
    private String sku;
    private MultipartFile photo;
    private String description;
    private Category category;

}
