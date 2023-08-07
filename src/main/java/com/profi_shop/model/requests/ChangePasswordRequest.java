package com.profi_shop.model.requests;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private Long userId;
    private String old_password;
    private String password;
    private String confirm_password;
}
