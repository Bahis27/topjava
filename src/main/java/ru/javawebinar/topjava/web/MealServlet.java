package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);
    private List<Meal> mealsList = MealsUtil.getMeals();
    private int caloriesPerDay = MealsUtil.getCaloriesPerDay();
    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");
        Map<LocalDate, Integer> caloriesSumByDate = MealsUtil.getCaloriesSumByDate(mealsList, caloriesPerDay);
        List<MealTo> mealToList = mealsList.stream()
                .map(meal -> MealsUtil.createWithExcess(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());

        request.setAttribute("mealtolist", mealToList);
        request.setAttribute("formatter", FORMATTER);
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }
}