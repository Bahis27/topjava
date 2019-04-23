package ru.javawebinar.topjava.web.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

@Component
public class UserMailValidator implements Validator {

    @Autowired
    UserRepository repository;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        User user = (User) target;

        User existing = repository.getByEmail(user.getEmail());
        if (existing != null) {
            errors.rejectValue("email", "User with this email already exists");
        }
    }
}
