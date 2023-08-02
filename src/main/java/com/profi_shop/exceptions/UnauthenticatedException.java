package com.profi_shop.exceptions;

import com.profi_shop.settings.Text;
import org.springframework.security.core.AuthenticationException;

public class UnauthenticatedException extends AuthenticationException {

    public UnauthenticatedException() {
        super("USER_NOT_AUTHENTICATED");
    }

    @Override
    public String getMessage(){
        return Text.get("USER_NOT_AUTHENTICATED");
    }

}
