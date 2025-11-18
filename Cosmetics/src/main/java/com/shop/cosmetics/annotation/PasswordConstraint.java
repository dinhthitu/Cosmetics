package com.shop.cosmetics.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordConstraint  implements ConstraintValidator<PasswordValidation, String> {@Override
public boolean isValid(String value, ConstraintValidatorContext context) {
    // check if string contains at least one digit, one lowercase letter, one uppercase letter, one special character and 8 characters long
    return value.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*()]).{8,}$");
}
}
