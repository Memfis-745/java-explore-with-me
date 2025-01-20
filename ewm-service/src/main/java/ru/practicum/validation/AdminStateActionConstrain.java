package ru.practicum.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AdminStateActionValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AdminStateActionConstrain {

    String message() default "В поле StateAction объекта класса UpdateEventAdminRequest передано не верное значение";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}