package ru.practicum.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EventSortValidator.class)
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EventSortConstrain {

    String message() default "В поле Sort объекта передано неверное значение";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}