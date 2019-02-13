package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealDao {

    public void add(Meal meal);

    public void delete(int id);

    public void update(int id, Meal meal);

    public List<Meal> getList();

    public Meal getById(int id);
}
