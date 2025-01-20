package ru.practicum.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = StatusValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface StatusConstrain {
    String message() default "В поле Status объекта класса RequestStatusUpdate передано не верное значение";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}