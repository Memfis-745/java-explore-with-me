package ru.practicum.validation;

import ru.practicum.event.AdminStateAction;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AdminStateActionValidator implements ConstraintValidator<AdminStateActionConstrain, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return AdminStateAction.from(value).isPresent();
    }
}