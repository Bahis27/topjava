package ru.javawebinar.topjava.validator;

import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.annotation.EmailNotExist;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailDoublesValidator implements ConstraintValidator<EmailNotExist, String> {
    @Autowired
    private UserRepository repository;

    @Override
    public void initialize(EmailNotExist constraintAnnotation) {

    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
//        return email != null && (email.length() > 0) && service.getByEmail(email) == null;
        return repository.getByEmail(email) == null;
    }
}
