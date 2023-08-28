package com.profi_shop.exceptions;


import com.profi_shop.settings.Text;

public class AccessDeniedException extends RuntimeException{
    public static final int FOREIGN_BRANCH = 1;
    public static final int FOREIGN_ACCOUNT = 2;
    public static final int LOW_AUTHORITIES = 3;
    public static final int ONLY_ADMIN_OF_SHOP = 4;
    public static final int FOREIGN_TRANSACTION = 5;
    public static final int ONLY_SUPER_ADMIN = 6;

    private final int code;

    public AccessDeniedException(int code) {
        this.code = code;
    }

    public String getMessage(){
        return switch (code) {
            case 1 -> Text.get("ERROR_FOREIGN_BRANCH");
            case 2 -> Text.get("ERROR_FOREIGN_ACCOUNT");
            case 3 -> Text.get("ERROR_LOW_AUTHORITIES");
            case 4 -> Text.get("ERROR_ONLY_ADMIN_OF_SHOP");
            case 5 -> Text.get("ERROR_FOREIGN_TRANSACTION");
            case 6 -> Text.get("ERROR_ONLY_SUPER_ADMIN");
            default -> "";
        };
    }
}
