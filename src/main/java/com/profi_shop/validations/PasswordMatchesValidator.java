package com.example.academia1_1.infrastructure.validations;


import com.example.academia1_1.domain.payload.request.CreateUserRequest;
import com.example.academia1_1.infrastructure.validations.annotations.PasswordMatches;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintsAnnotation){
    }
    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        CreateUserRequest signUpRequest = (CreateUserRequest) o;
        return signUpRequest.getPassword().equals(signUpRequest.getConfirm_password());
    }
}
