package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.ServiceMeal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDao {
    private Map<Integer, Meal> connection;
    private static AtomicInteger idCounter;

    public MealDao() {
        this.connection = new ServiceMeal().getConnection();
        idCounter = new AtomicInteger(this.connection.size());
    }

    public void addMeal(Meal meal) {
        connection.put(idCounter.incrementAndGet(), meal);
    }

    public void deleteMeal(int mealId) {
        connection.remove(mealId);
    }

    public void updateMeal(int mealId, Meal meal) {
        connection.replace(mealId, meal);
    }

    public List<Meal> getMeals() {
        return new ArrayList<>(connection.values());
    }

    public Meal getMealById(int mealId) {
        Meal meal = connection.get(mealId);
        return new Meal(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories());
    }

    public void clrearResources() {
        connection = null;
    }

    public int getCount() {
        return connection.size();
    }

}