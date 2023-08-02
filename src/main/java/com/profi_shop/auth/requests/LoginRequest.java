package com.profi_shop.auth.requests;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
