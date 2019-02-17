package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);
    private final Meal DEFAULT_MEAL = new Meal(LocalDateTime.now(), "not exist meal", 0, 0);

    {
        MealsUtil.MEALS.forEach(meal -> save(meal, meal.getUserId()));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (userId != meal.getUserId())
            return null;
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // treat case: update, but absent in storage
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        if (userId != repository.getOrDefault(id, DEFAULT_MEAL).getUserId())
            return false;
        return null != repository.remove(id);
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = repository.getOrDefault(id, DEFAULT_MEAL);
        return userId == meal.getUserId() ? meal : null;
    }

    @Override
    public Collection<Meal> getAll() {
        return repository.values().stream()
                .filter(meal -> meal.getUserId() == SecurityUtil.getAuthUserId())
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}