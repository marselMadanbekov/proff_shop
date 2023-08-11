package com.profi_shop.exceptions;


import com.profi_shop.settings.Text;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidDataException extends RuntimeException{
    public static final int INVALID_NAME = 1;
    public static final int INVALID_AGE = 2;
    public static final int INVALID_EMAIL = 3;
    public static final int EMPTY_NAME = 4;
    public static final int INVALID_PHONE_NUMBER = 5;
    public static final int TARGET_USER_NOT_EQUAL_REQUESTER = 6;
    public static final int PASSWORDS_DONT_MATCH = 7;
    public static final int INVALID_PASSWORD = 8;
    public static final int INVALID_PHOTO = 9;
    public static final int EMPTY_CART = 10;

    private final int code;
    public InvalidDataException(int code){
        this.code = code;
    }
    public String getMessage(){
        return switch (code) {
            case 1 -> Text.get("ERROR_INVALID_NAME");
            case 2 -> Text.get("ERROR_INVALID_AGE");
            case 3 -> Text.get("ERROR_INVALID_EMAIL");
            case 4 -> Text.get("ERROR_NAME_EMPTY");
            case 5 -> Text.get("ERROR_INVALID_PHONE_NUMBER");
            case 6 -> Text.get("ERROR_TARGET_USER_NOT_EQUAL_REQUESTER");
            case 7 -> Text.get("ERROR_PASSWORD_DONT_MATCH");
            case 8 -> Text.get("ERROR_INVALID_PASSWORD");
            case 9 -> Text.get("ERROR_INVALID_PHOTO");
            case 10 -> Text.get("ERROR_EMPTY_CART");
            default -> null;
        };
    }
}
