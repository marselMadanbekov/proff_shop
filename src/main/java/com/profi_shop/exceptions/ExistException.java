package com.profi_shop.exceptions;


import com.profi_shop.settings.Text;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ExistException extends Exception{
    public static final int USER_EXISTS = 1;
    public static final int SHIPMENT_EXISTS = 2;

    public static final int SIZE_OF_PRODUCT_EXIST = 3;
    public static final int PRODUCT_SKU_EXIST = 4;
    public static final int WISHLIST_CONTAINS_PRODUCT = 5;
    public static final int PRODUCT_IS_ALREADY_IN_STOCK = 6;
    public static final int CART_ITEM_EXIST = 7;
    public static final int SPECIFICATION_EXIST = 8;
    public static final int ORDER_ITEM_EXISTS = 9;
    public static final int PRODUCT_IS_ALREADY_IN_GROUP = 10;
    public static final int CATEGORY_EXISTS = 11;


    private final int code;


    public ExistException(int code) {
        this.code = code;
    }

    public String getMessage(){
        return switch (code) {
            case 1 -> Text.get("ERROR_USER_EXISTS");
            case 2 -> Text.get("ERROR_SHIPMENT_EXISTS");
            case 3 -> Text.get("ERROR_SIZE_OF_PRODUCT_EXIST");
            case 4 -> Text.get("ERROR_PRODUCT_SKU_EXIST");
            case 5 -> Text.get("ERROR_WISHLIST_CONTAINS_PRODUCT");
            case 6 -> Text.get("ERROR_PRODUCT_IS_ALREADY_IN_STOCK");
            case 7 -> Text.get("ERROR_CART_ITEM_EXIST");
            case 8 -> Text.get("ERROR_SPECIFICATION_EXISTS");
            case 9 -> Text.get("ERROR_ORDER_ITEM_EXISTS");
            case 10 -> Text.get("ERROR_PRODUCT_IS_ALREADY_IN_GROUP");
            case 11 -> Text.get("ERROR_CATEGORY_EXISTS");
            default -> "";
        };
    }
}
