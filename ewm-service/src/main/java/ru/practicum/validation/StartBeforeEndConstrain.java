package ru.practicum.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = StartBeforeEndValidator.class)
@Target({ElementType.PARAMETER, ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface StartBeforeEndConstrain {
    String message() default "Параметр rangeStart не может быть пожзе rangeEnd";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}