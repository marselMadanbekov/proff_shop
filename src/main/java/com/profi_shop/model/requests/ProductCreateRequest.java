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
    private Category category;
    private String size;
    private String color;
    private String tag;
    private String brand;
    private MultipartFile photo;
    private String description;

}
