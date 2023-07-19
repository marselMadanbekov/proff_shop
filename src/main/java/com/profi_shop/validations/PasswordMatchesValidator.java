package com.profi_shop.validations;


import com.profi_shop.auth.requests.AbstractUser;
import com.profi_shop.validations.annotations.PasswordMatches;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintsAnnotation){
    }
    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        AbstractUser signUpRequest = (AbstractUser) o;
        return signUpRequest.getPassword().equals(signUpRequest.getConfirm_password());
    }
}
