package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.data.MemoryDb;
import ru.javawebinar.topjava.model.Meal;

import java.util.Map;

public class ServiceMeal {
    private Map<Integer, Meal> connection;

    public ServiceMeal() {
        this.connection = new MemoryDb().connectToDb();
    }

    public Map<Integer, Meal> getConnection() {
        return connection;
    }
}