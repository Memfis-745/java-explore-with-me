package ru.practicum.validation;

import ru.practicum.event.dto.Params;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class StartBeforeEndValidator implements ConstraintValidator<StartBeforeEndConstrain, Params> {

    @Override
    public boolean isValid(Params value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        LocalDateTime start = value.getStartDateTime();
        LocalDateTime end = value.getEndDateTime();

        return (end == null || start == null) || !end.isBefore(start);
    }
}