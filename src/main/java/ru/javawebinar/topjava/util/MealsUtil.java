package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class MealsUtil {
    public static final List<Meal> MEALS1 = Arrays.asList(
            new Meal(LocalDateTime.of(2018, Month.FEBRUARY, 10, 0, 0), "Завтрак", 500),
            new Meal(LocalDateTime.of(2018, Month.FEBRUARY, 10, 13, 20), "Обед", 1000),
            new Meal(LocalDateTime.of(2018, Month.FEBRUARY, 10, 20, 0), "Ужин", 500),
            new Meal(LocalDateTime.of(2018, Month.FEBRUARY, 11, 10, 0), "Завтрак", 1000),
            new Meal(LocalDateTime.of(2018, Month.FEBRUARY, 11, 13, 10), "Обед", 500),
            new Meal(LocalDateTime.of(2018, Month.FEBRUARY, 11, 20, 0), "Ужин", 510),
            new Meal(LocalDateTime.of(2018, Month.FEBRUARY, 12, 10, 0), "Завтрак", 1000),
            new Meal(LocalDateTime.of(2018, Month.FEBRUARY, 12, 13, 0), "Обед", 500),
            new Meal(LocalDateTime.of(2018, Month.FEBRUARY, 12, 23, 59), "Ужин", 300)
    );

    public static final List<Meal> MEALS2 = Arrays.asList(
            new Meal(LocalDateTime.of(2018, Month.FEBRUARY, 10, 10, 0), "Завтрак", 500),
            new Meal(LocalDateTime.of(2018, Month.FEBRUARY, 10, 13, 0), "Обед", 1000),
            new Meal(LocalDateTime.of(2018, Month.FEBRUARY, 10, 20, 0), "Ужин", 500),
            new Meal(LocalDateTime.of(2018, Month.FEBRUARY, 11, 10, 0), "Завтрак", 1000),
            new Meal(LocalDateTime.of(2018, Month.FEBRUARY, 11, 13, 0), "Обед", 500),
            new Meal(LocalDateTime.of(2018, Month.FEBRUARY, 11, 20, 0), "Ужин", 510),
            new Meal(LocalDateTime.of(2018, Month.FEBRUARY, 12, 10, 0), "Завтрак", 1000),
            new Meal(LocalDateTime.of(2018, Month.FEBRUARY, 12, 13, 0), "Обед", 500),
            new Meal(LocalDateTime.of(2018, Month.FEBRUARY, 12, 20, 0), "Ужин", 300)

    );

    public static final int DEFAULT_CALORIES_PER_DAY = 2000;

    //дергается из сервлета
    public static List<MealTo> getWithExcess(Collection<Meal> meals, int caloriesPerDay) {
        return getFilteredWithExcess(meals, caloriesPerDay, meal -> true);
    }

    public static List<MealTo> getFilteredWithExcess(Collection<Meal> meals, int caloriesPerDay, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        return getFilteredWithExcess(meals, caloriesPerDay, meal -> DateTimeUtil.isBetween(meal.getDate(), meal.getTime(), startDate, endDate, startTime, endTime));
    }

    private static List<MealTo> getFilteredWithExcess(Collection<Meal> meals, int caloriesPerDay, Predicate<Meal> filter) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
//                      Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );

        return meals.stream()
                .filter(filter)
                .map(meal -> createWithExcess(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(toList());
    }

    public static MealTo createWithExcess(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}
