package com.profi_shop.dto;

import com.profi_shop.model.ProductVariation;
import com.profi_shop.model.StoreHouse;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ProductDetailsDTO {
    private Long id;
    private String name;
    private String sku;

    private List<String> photos;
    private int discount;

    private int newPrice;
    private int oldPrice;

    private String color;
    private List<ProductVariation> productVariations;
    private Map<String,String> specifications;
    private int averageRating;

    private boolean available;
    private Map<ProductVariation,Integer> productVariationCount;
    private List<StoreHouse> storeHouses;
    private String brand;

    private String description;
    private CategoryDTO category;
}
