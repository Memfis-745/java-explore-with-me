package ru.practicum.validation;

import ru.practicum.comments.SortComments;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.practicum.comments.SortComments;

public class CommentValidator implements ConstraintValidator<CommentSortConstrain, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return SortComments.from(value).isPresent();
    }
}