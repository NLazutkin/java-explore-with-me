package ru.practicum.ewm.base.util.IfNotNullIsNotEmpty;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IfNotNullIsNotEmptyConstraintValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IfNotNullIsNotEmpty {
    String message() default "Если поле не содержит null, то оно не должно быть пустым";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
