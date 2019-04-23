package ru.javawebinar.topjava.annotation;

import ru.javawebinar.topjava.validator.EmailDoublesValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailDoublesValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailNotExist {
    String message() default "This email already exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}