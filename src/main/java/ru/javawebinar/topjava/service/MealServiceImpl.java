package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
public class MealServiceImpl implements MealService {

    private final MealRepository repository;

    @Autowired
    public MealServiceImpl(MealRepository repository) {
        this.repository = repository;
    }

    @Override
    public Meal create(Meal meal, int authUserId) {
        return repository.save(meal, authUserId);
    }

    @Override
    public boolean delete(int id, int authUserId) throws NotFoundException {
        return repository.delete(id, authUserId);
    }

    @Override
    public Meal get(int id, int authUserId) throws NotFoundException {
        return repository.get(id, authUserId);
    }

    @Override
    public Meal update(Meal meal, int authUserId) {
        return repository.save(meal, authUserId);
    }

    @Override
    public List<Meal> getAll(int authUserId) {
        return new ArrayList<>(repository.getAll(authUserId));
    }
}