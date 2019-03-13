package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.UserServiceTest;

import java.util.ArrayList;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ActiveProfiles(profiles = Profiles.DATAJPA)
public class DataJpaUserServiceTest extends UserServiceTest {

    @Test
    public void getAllMealsByUserId() {
        List<Meal> meals = userService.getUserByIdWithAllMeals(USER_ID).getMeals();
        List<Meal> copy = new ArrayList<>(MEALS);
        assertMatchIgnoringOrder(meals, copy);
    }

    @Test
    public void getAllMealsByUserIdWithNoMeals() {
        mealService.delete(ADMIN_MEAL_ID, ADMIN_ID);
        mealService.delete(ADMIN_MEAL_ID + 1, ADMIN_ID);
        List<Meal> meals = userService.getUserByIdWithAllMeals(ADMIN_ID).getMeals();
        List<Meal> copy = new ArrayList<>(NO_MEALS);
        assertMatchIgnoringOrder(meals, copy);
    }
}
