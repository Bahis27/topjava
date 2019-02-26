package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration(
        "classpath:spring/spring-db.xml"
)
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static  {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void create() {
        Meal newMeal = new Meal(null, LocalDateTime.of(2019, 7, 1, 10,  1, 35), "test", 500);
        Meal inBaseNewMeal = service.create(newMeal, ADMIN_ID);
        assertMealMatch(service.getAll(ADMIN_ID), inBaseNewMeal, MEAL06, MEAL05, MEAL04);
    }

    @Test
    public void get() {
        Meal meal = service.get(MEAL01_ID, USER_ID);
        assertMealMatch(meal, MEAL01);
    }

    @Test(expected = NotFoundException.class)
    public void getNotMyMeal() {
        service.get(MEAL05_ID, USER_ID);
    }

    @Test
    public void delete() {
        service.delete(MEAL01_ID, USER_ID);
        List<Meal> all = service.getAll(USER_ID);
        assertMealMatch(all, MEAL03, MEAL02);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotMyMeal() {
        service.delete(MEAL01_ID, ADMIN_ID);
    }

    @Test
    public void getBetweenDateTimes() {
        List<Meal> meals = service.getBetweenDateTimes(START_DATE_TIME, END_DATE_TIME, USER_ID);
        assertMealMatch(meals, MEAL02);
    }

    @Test
    public void update() {
        Meal updated = new Meal(MEAL01);
        updated.setDescription("TEST");
        service.update(updated, USER_ID);
        assertMealMatch(service.get(MEAL01_ID, USER_ID), updated);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotMyMeal() {
        Meal updated = new Meal(MEAL01);
        updated.setDescription("TEST");
        service.update(updated, ADMIN_ID);
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(USER_ID);
        assertMealMatch(all, MEAL03, MEAL02, MEAL01);
    }
}