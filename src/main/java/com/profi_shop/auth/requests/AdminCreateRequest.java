package com.profi_shop.auth.requests;

import com.profi_shop.validations.annotations.PasswordMatches;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@PasswordMatches
public class AdminCreateRequest extends AbstractUser{
    private Long storeId;
}
