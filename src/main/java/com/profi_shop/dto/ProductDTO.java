package com.profi_shop.dto;

import com.profi_shop.model.Category;
import com.profi_shop.model.StoreHouse;
import com.profi_shop.model.enums.ProductSize;
import lombok.Data;

import java.util.List;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private String sku;

    private List<String> photos;
    private int discount;

    private int newPrice;
    private int oldPrice;

    private String color;
    private ProductSize size;
    private int averageRating;

    private boolean inCart;
    private boolean inWishlist;
    private List<StoreHouse> storeHouses;

    private String description;
    private Category category;
}
