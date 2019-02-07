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
        List<UserMealWithExceed> filteredWithExceeded = getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        filteredWithExceeded.forEach(System.out::println);
    }

    public static List<UserMealWithExceed> getFilteredWithExceededByCycles(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> amountOfCaloriesPerDay = new HashMap<>();
        mealList.forEach(
                u -> amountOfCaloriesPerDay.merge(u.getDate(),
                        u.getCalories(),
                        Integer::sum)
        );
        List<UserMealWithExceed> result = new ArrayList<>();
        mealList.forEach(
                u -> {
                    if (TimeUtil.isBetween(u.getTime(), startTime, endTime)) {
                        result.add(new UserMealWithExceed(u,
                                amountOfCaloriesPerDay.get(u.getDate()) > caloriesPerDay));
                    }
                }
        );
        return result;
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> amountOfCaloriesPerDay = mealList.stream()
                .collect(Collectors.groupingBy(
                        UserMeal::getDate,
                        Collectors.summingInt(UserMeal::getCalories)
                ));
        return mealList.stream()
                .filter(u -> TimeUtil.isBetween(u.getTime(), startTime, endTime))
                .map(u -> new UserMealWithExceed(u,
                        amountOfCaloriesPerDay.get(u.getDate()) > caloriesPerDay)
                )
                .collect(Collectors.toList());
    }
}