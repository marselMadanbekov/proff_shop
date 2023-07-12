package com.example.academia1_1.infrastructure.validations.annotations;


import com.example.academia1_1.infrastructure.validations.PasswordMatchesValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE,ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchesValidator.class)
@Documented
public @interface PasswordMatches {
    String message() default "Password do not match!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
