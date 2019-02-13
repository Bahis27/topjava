package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealRepository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);
    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    private final DateTimeFormatter FORMATTER_TO_EDIT_PAGE = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm");
    private int caloriesPerDay = MealsUtil.getCaloriesPerDay();
    private MealDao mealDao;

    @Override
    public void init() throws ServletException {
        log.debug("init " + getClass().getSimpleName());
        mealDao = new MealRepository();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals, get");
        request.setCharacterEncoding("UTF-8");
        String s = request.getParameter("action");
        if (s != null) {
            switch (s) {
                case "delete":
                    mealDao.delete(Integer.parseInt(request.getParameter("mealtoid")));
                    break;
                case "edit":
                    int id = Integer.parseInt(request.getParameter("mealtoid"));
                    request.setAttribute("meal", mealDao.getById(id));
                    request.setAttribute("formatter", FORMATTER_TO_EDIT_PAGE);
                    request.getRequestDispatcher("/mealEdit.jsp").forward(request, response);
                    return;
                default:
                    break;
            }
        }
        request.setAttribute("formatter", FORMATTER);
        request.setAttribute("mealtolist", MealsUtil.getAllMealsTo(mealDao.getList(), caloriesPerDay));
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals, post");
        request.setCharacterEncoding("UTF-8");
        String idString = request.getParameter("id");
        String date = request.getParameter("dateTime");
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        int id = 0;
        if (idString == null) {
            Meal meal = new Meal(id, LocalDateTime.parse(date), description, calories);
            mealDao.add(meal);
        } else {
            id = Integer.parseInt(idString);
            Meal meal = new Meal(id, LocalDateTime.parse(date), description, calories);
            mealDao.update(id, meal);
        }
        response.sendRedirect("meals");
    }
}