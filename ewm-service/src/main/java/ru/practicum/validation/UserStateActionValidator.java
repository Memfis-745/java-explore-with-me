package ru.practicum.validation;


import ru.practicum.event.UserStateAction;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserStateActionValidator implements ConstraintValidator<UserStateActionConstrain, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return UserStateAction.from(value).isPresent();
    }
}