package ru.practicum.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CommentValidator.class)
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CommentSortConstrain {

    String message() default "В параметр Sort запроса передано неверное значение";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}