package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);
    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private int caloriesPerDay = MealsUtil.getCaloriesPerDay();
    private MealDao mealDao;
    private volatile static AtomicInteger idCount;

    @Override
    public void init() throws ServletException {
        log.debug("init " + getClass().getSimpleName());
        mealDao = new MealDao();
        idCount = new AtomicInteger(mealDao.getCount());
    }

    @Override
    public void destroy() {
        log.debug("destroy " + getClass().getSimpleName());
        mealDao.clrearResources();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");
        request.setCharacterEncoding("UTF-8");
        String s = request.getParameter("action");
        if (s != null && s.equals("delete")) {
            deleteMeal(Integer.parseInt(request.getParameter("mealtoid")));
            request.setAttribute("mealtolist", getMealsTo());
            request.setAttribute("formatter", FORMATTER);
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        } else if (s != null && s.equals("edit")){
            request.setAttribute("formatter", FORMATTER);
            int id = Integer.parseInt(request.getParameter("mealtoid"));
            request.setAttribute("meal", getMealById(id));
            request.getRequestDispatcher("/mealEdit.jsp").forward(request, response);
        } else {
            request.setAttribute("mealtolist", getMealsTo());
            request.setAttribute("formatter", FORMATTER);
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String idString = request.getParameter("id");
        String date = request.getParameter("dateTime");
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));

        if (idString == null) {
            int id = idCount.incrementAndGet();
            Meal meal = new Meal(id, LocalDateTime.parse(date, FORMATTER), description, calories);
            addMeal(meal);
            response.sendRedirect("/topjava/meals");
        } else {
            int id = Integer.parseInt(idString);
            Meal meal = new Meal(id, LocalDateTime.parse(date, FORMATTER), description, calories);
            updateMeal(id, meal);
            response.sendRedirect("/topjava/meals");
        }
    }

    private void addMeal(Meal meal) {
        mealDao.addMeal(meal);
    }

    private void deleteMeal(int id) {
        mealDao.deleteMeal(id);
    }

    private void updateMeal(int id, Meal meal) {
        mealDao.updateMeal(id, meal);
    }

    private List<MealTo> getMealsTo() {
        return MealsUtil.getAllMealsTo(mealDao.getMeals(), caloriesPerDay);
    }

    private Meal getMealById(int id) {
        return mealDao.getMealById(id);
    }

}