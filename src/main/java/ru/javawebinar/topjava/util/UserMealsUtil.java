package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
    }

    public static List<UserMealWithExceed> getFilteredWithExceededWithCycles(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> amountOfCaloriesPerDay = new HashMap<>();
        mealList.forEach(
                u -> amountOfCaloriesPerDay.merge(u.getDateTime().toLocalDate(),
                        u.getCalories(),
                        (oldValue, newValue) -> oldValue + newValue)
        );
        List<UserMealWithExceed> result = new ArrayList<>();
        mealList.forEach(
                u -> {
                    if (TimeUtil.isBetween(u.getDateTime().toLocalTime(), startTime, endTime)) {
                        result.add(new UserMealWithExceed(u.getDateTime(),
                                u.getDescription(),
                                u.getCalories(),
                                amountOfCaloriesPerDay.get(u.getDateTime().toLocalDate()) > caloriesPerDay));
                    }
                }
        );
        return result;
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> amountOfCaloriesPerDay = mealList.stream()
                .collect(Collectors.groupingBy(
                        u -> u.getDateTime().toLocalDate(),
                        Collectors.summingInt(UserMeal::getCalories)
                ));
        return mealList.stream()
                .filter(t -> TimeUtil.isBetween(t.getDateTime().toLocalTime(), startTime, endTime))
                .map(u -> new UserMealWithExceed(u.getDateTime(),
                        u.getDescription(),
                        u.getCalories(),
                        amountOfCaloriesPerDay.get(u.getDateTime().toLocalDate()) > caloriesPerDay)
                )
                .collect(Collectors.toList());
    }
}