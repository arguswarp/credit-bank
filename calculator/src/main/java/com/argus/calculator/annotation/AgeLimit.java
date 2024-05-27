package com.argus.calculator.annotation;

import com.argus.calculator.model.validator.AgeLimitValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AgeLimitValidator.class)
public @interface AgeLimit {
    int minimumAge() default 18;

    String message() default "Возраст должен быть больше 18 лет";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
