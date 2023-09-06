package com.profi_shop.exceptions;

import com.profi_shop.settings.Text;

import java.sql.Date;

public class CouponException extends RuntimeException{
    public final static int COUPON_ALREADY_USED = 0;
    public final static int COUPON_EXPIRED = 1;
    public final static int COUPON_NOT_APPLICABLE_TO_DISCOUNTED_PRODUCTS = 2;

    public final static int COUPON_ROOT_IS_INACTIVE = 3;
    private final int code;
    private final String message;
    public CouponException(int code){
        this.code = code;
        message = null;
    }
    public CouponException(Date message){
        this.message = "Срок годности купона истек " + message;
        code = -1;
    }

    public String getMessage(){
        return switch (code){
            case -1 -> message;
            case 0 -> Text.get("ERROR_COUPON_ALREADY_USED");
            case 1 -> Text.get("ERROR_COUPON_EXPIRED");
            case 2 -> Text.get("ERROR_COUPON_NOT_APPLICABLE_TO_DISCOUNTED_PRODUCTS");
            case 3 -> Text.get("ERROR_COUPON_ROOT_INACTIVE");
            default -> "";
        };
    }
}
