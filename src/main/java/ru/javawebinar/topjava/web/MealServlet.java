package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    //private MealRepository repository;
    private ConfigurableApplicationContext applicationContext;
    private MealRestController controller;


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        //repository = new InMemoryMealRepositoryImpl();
        applicationContext = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        controller = applicationContext.getBean(MealRestController.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        int userId = Integer.parseInt(request.getParameter("userid"));

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")),
                userId);

        log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        //repository.save(meal, meal.getUserId());
        if (meal.isNew()) {
            controller.create(meal, userId);
        } else {
            controller.update(meal, Integer.parseInt(id), userId);
        }
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String userId = request.getParameter("userid");

        switch (action == null || userId == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                //repository.delete(id, Integer.parseInt(userId));
                controller.delete(id, Integer.parseInt(userId));
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000, SecurityUtil.getAuthUserId()) :
                        //repository.get(getId(request), Integer.parseInt(userId));
                        controller.get(getId(request), Integer.parseInt(userId));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "register":
                SecurityUtil.setAuthUserId(Integer.parseInt(request.getParameter("userid")));
            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals",
                        //MealsUtil.getWithExcess(repository.getAll(), MealsUtil.DEFAULT_CALORIES_PER_DAY));
                        MealsUtil.getWithExcess(controller.getAll(), MealsUtil.DEFAULT_CALORIES_PER_DAY));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    @Override
    public void destroy() {
        applicationContext.close();
        super.destroy();
    }
}