package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.service.UserServiceTest;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

import java.util.ArrayList;
import java.util.List;

@ActiveProfiles(profiles = Profiles.DATAJPA)
public class DataJpaUserServiceTest extends UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private MealService mealService;

    @Test
    public void getAllMealsByUserId() {
        List<Meal> meals = userService.getAllMealsByUserId(USER_ID);
        List<Meal> copy = new ArrayList<>(MEALS);
        assertMatchIgnoringOrder(meals, copy);
    }

    @Test
    public void getAllMealsByUserIdWithNoMeals() {
        mealService.delete(ADMIN_MEAL_ID, ADMIN_ID);
        mealService.delete(ADMIN_MEAL_ID + 1, ADMIN_ID);
        List<Meal> meals = userService.getAllMealsByUserId(ADMIN_ID);
        List<Meal> copy = new ArrayList<>(NO_MEALS);
        assertMatchIgnoringOrder(meals, copy);
    }
}
