package com.profi_shop.exceptions;


import com.profi_shop.settings.Text;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SearchException extends RuntimeException{
    public static final int USER_NOT_FOUND = 1;
    public static final int STORE_NOT_FOUND = 2;
    public static final int PRODUCT_NOT_FOUND = 3;
    public static final int ORDER_NOT_FOUND = 4;
    public static final int CATEGORY_NOT_FOUND = 5;

    public static final int CONSUMPTION_NOT_FOUND = 6;
    public static final int STOCK_NOT_FOUND = 7;
    public static final int SHIPMENT_NOT_FOUND = 8;
    public static final int COUPON_TEMPLATE_NOT_FOUND = 9;
    public static final int WISHLIST_NOT_FOUND = 10;
    public static final int PRODUCT_VARIATION_NOT_FOUND = 11;
    public static final int PRODUCT_GROUP_NOT_FOUND = 12;
    public static final int NOTIFICATION_NOT_FOUND = 13;
    public static final int ORDER_ITEM_NOT_FOUND = 14;


    private final int code;


    public SearchException(int code) {
        this.code = code;
    }


    public String getMessage(){
        return switch (code) {
            case 1 -> Text.get("ERROR_USER_NOT_FOUND");
            case 2 -> Text.get("ERROR_STORE_NOT_FOUND");
            case 3 -> Text.get("ERROR_PRODUCT_NOT_FOUND");
            case 4 -> Text.get("ERROR_ORDER_NOT_FOUND");
            case 5 -> Text.get("ERROR_CATEGORY_NOT_FOUND");
            case 6 -> Text.get("ERROR_CONSUMPTION_NOT_FOUND");
            case 7 -> Text.get("ERROR_STOCK_NOT_FOUND");
            case 8 -> Text.get("ERROR_SHIPMENT_NOT_FOUND");
            case 9 -> Text.get("ERROR_COUPON_TEMPLATE_NOT_FOUND");
            case 10 -> Text.get("ERROR_WISHLIST_NOT_FOUND");
            case 11 -> Text.get("ERROR_PRODUCT_VARIATION_NOT_FOUND");
            case 12 -> Text.get("ERROR_PRODUCT_GROUP_NOT_FOUND");
            case 13 -> Text.get("ERROR_NOTIFICATION_NOT_FOUND");
            case 14 -> Text.get("ERROR_ORDER_ITEM_NOT_FOUND");
            default -> "";
        };
    }
}
