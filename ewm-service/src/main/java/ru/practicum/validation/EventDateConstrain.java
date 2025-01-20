package ru.practicum.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EventDateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EventDateConstrain {
    String message() default "Дата и время начала события не может быть раньше, чем через два часа";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}