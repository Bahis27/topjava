package ru.javawebinar.topjava.service.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.service.others.UserServiceImpl;

import java.util.List;

@Service
public class DataJpaUserServiceImpl extends UserServiceImpl {

    private final UserRepository repository;

    @Autowired
    public DataJpaUserServiceImpl(UserRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public List<Meal> getAllMealsByUserId(int userId) {
        return repository.getAllMealsByUserId(userId);
    }
}