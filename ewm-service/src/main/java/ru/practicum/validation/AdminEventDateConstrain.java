package ru.practicum.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AdminEventDateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AdminEventDateConstrain {

    String message() default "Дата и время события не может быть раньше, чем через час";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}