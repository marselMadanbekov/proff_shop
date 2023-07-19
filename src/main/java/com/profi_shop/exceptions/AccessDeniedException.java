package com.profi_shop.exceptions;


import com.profi_shop.settings.Text;

public class AccessDeniedException extends RuntimeException{
    public static final int FOREIGN_BRANCH = 1;
    public static final int FOREIGN_ACCOUNT = 2;
    public static final int LOW_AUTHORITIES = 3;

    private final int code;

    public AccessDeniedException(int code) {
        this.code = code;
    }

    public String getMessage(){
        return switch (code) {
            case 1 -> Text.get("ERROR_FOREIGN_BRANCH");
            case 2 -> Text.get("ERROR_FOREIGN_ACCOUNT");
            case 3 -> Text.get("ERROR_LOW_AUTHORITIES");
            default -> "";
        };
    }
}
