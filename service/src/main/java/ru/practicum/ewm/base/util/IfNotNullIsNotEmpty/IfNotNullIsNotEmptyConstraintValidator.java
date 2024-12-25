package ru.practicum.ewm.base.util.IfNotNullIsNotEmpty;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IfNotNullIsNotEmptyConstraintValidator implements ConstraintValidator<IfNotNullIsNotEmpty, String> {
    @Override
    public boolean isValid(String text, ConstraintValidatorContext constraintValidatorContext) {
        if (text == null) {
            return true;
        } else {
            return !text.isBlank();
        }
    }
}
