package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

public interface MealService {

    Meal create(Meal meal, int userId);

    boolean delete(int id, int userId) throws NotFoundException;

    Meal get(int id, int userId) throws NotFoundException;

    Meal update(Meal meal, int userId);

    List<Meal> getAll();
}