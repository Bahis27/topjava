package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS1.forEach(meal -> save(meal, 1));
        MealsUtil.MEALS2.forEach(meal -> save(meal, 2));
    }

    @Override
    public Meal save(Meal meal, int authUserIdd) {
        repository.putIfAbsent(authUserIdd, new ConcurrentHashMap<>());
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.get(authUserIdd).put(meal.getId(), meal);
            return meal;
        }
        // treat case: update, but absent in storage
        return repository.get(authUserIdd).computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int authUserId) {
        return null != repository.get(authUserId).remove(id);
    }

    @Override
    public Meal get(int id, int authUserId) {
        return repository.get(authUserId).get(id);
    }

    @Override
    public List<Meal> getAll(int authUserId) {
        return repository.getOrDefault(authUserId, new ConcurrentHashMap<>()).values().stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}