package ru.javawebinar.topjava.service.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.service.others.MealServiceImpl;

@Service
public class DataJpaMealServiceImpl extends MealServiceImpl {

    private final MealRepository repository;

    @Autowired
    public DataJpaMealServiceImpl(MealRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public User getUserByMealId(int id) {
        return repository.getUserByMealId(id);
    }
}