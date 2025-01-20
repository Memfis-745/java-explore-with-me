package ru.practicum.validation;

import ru.practicum.event.Sort;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EventSortValidator implements ConstraintValidator<EventSortConstrain, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return Sort.from(value).isPresent();
    }
}