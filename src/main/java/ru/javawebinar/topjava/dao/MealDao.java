package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.data.MealTestData;
import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDao {
    private Map<Integer, Meal> testData;
    private AtomicInteger idCounter;

    public MealDao() {
        this.testData = new MealTestData().getData();
        idCounter = new AtomicInteger(this.testData.size());
    }

    public void add(Meal meal) {
        int id = idCounter.incrementAndGet();
        meal.setId(id);
        testData.put(id, meal);
    }

    public void delete(int mealId) {
        testData.remove(mealId);
    }

    public void update(int mealId, Meal meal) {
        testData.replace(mealId, meal);
    }

    public List<Meal> getList() {
        return new ArrayList<>(testData.values());
    }

    public Meal getById(int mealId) {
        Meal meal = testData.get(mealId);
        return new Meal(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories());
    }
}