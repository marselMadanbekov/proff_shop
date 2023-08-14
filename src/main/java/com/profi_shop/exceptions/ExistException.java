package com.profi_shop.exceptions;


import com.profi_shop.settings.Text;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ExistException extends Exception{
    public static final int USER_EXISTS = 1;
    public static final int SHIPMENT_EXISTS = 2;

    public static final int SIZE_OF_PRODUCT_EXIST = 3;


    private final int code;


    public ExistException(int code) {
        this.code = code;
    }

    public String getMessage(){
        return switch (code) {
            case 1 -> Text.get("ERROR_USER_EXISTS");
            case 2 -> Text.get("ERROR_SHIPMENT_EXISTS");
            case 3 -> Text.get("ERROR_SIZE_OF_PRODUCT_EXIST");
            default -> "";
        };
    }
}
