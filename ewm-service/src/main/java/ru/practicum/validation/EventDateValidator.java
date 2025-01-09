package ru.practicum.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.practicum.exceptions.Constants;

import java.time.LocalDateTime;

public class EventDateValidator implements ConstraintValidator<EventDateConstrain, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        LocalDateTime dateTime = LocalDateTime.parse(value, Constants.DATE_FORMAT);
        if (dateTime.isAfter(LocalDateTime.now().plusHours(2))) {
            return true;
        }
        return false;
    }
}