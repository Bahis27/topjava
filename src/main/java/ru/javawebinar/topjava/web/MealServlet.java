package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private ConfigurableApplicationContext applicationContext;
    private MealRestController controller;


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        applicationContext = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        controller = applicationContext.getBean(MealRestController.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        if (action != null && action.equals("filter")) {
            String sStartDate = request.getParameter("startdate");
            String sEndDate = request.getParameter("enddate");
            String sStartTime = request.getParameter("starttime");
            String sEndTime = request.getParameter("endtime");

            LocalDate startDate = (sStartDate.length() == 0) ? LocalDate.MIN : LocalDate.parse(sStartDate);
            LocalDate endDate = (sEndDate.length() == 0) ? LocalDate.MAX : LocalDate.parse(sEndDate);
            LocalTime startTime = (sStartTime.length() == 0) ? LocalTime.MIN : LocalTime.parse(sStartTime);
            LocalTime endTime = (sEndTime.length() == 0) ? LocalTime.MAX : LocalTime.parse(sEndTime);

            if (startTime.isAfter(endTime))
                endTime = LocalTime.MAX;
            if (startDate.isAfter(endDate))
                endDate = LocalDate.MAX;

            log.info("getAllFiltered");
            List<MealTo> list = MealsUtil.getFilteredWithExcess(controller.getAll(SecurityUtil.getAuthUserId()),
                            MealsUtil.DEFAULT_CALORIES_PER_DAY, startDate, endDate);
            request.setAttribute("meals", MealsUtil.getFilteredWithExcess(MealsUtil.convetToMeal(list), MealsUtil.DEFAULT_CALORIES_PER_DAY, startTime, endTime));

            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        } else {
            String id = request.getParameter("id");
            Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories")),
                    SecurityUtil.getAuthUserId());
            log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
            if (meal.isNew()) {
                controller.create(meal, SecurityUtil.getAuthUserId());
            } else {
                controller.update(meal, SecurityUtil.getAuthUserId(), meal.getId());
            }
            response.sendRedirect("meals");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                controller.delete(id, SecurityUtil.getAuthUserId());
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000, SecurityUtil.getAuthUserId()) :
                        controller.get(getId(request), SecurityUtil.getAuthUserId());
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals",
                        MealsUtil.getWithExcess(controller.getAll(SecurityUtil.getAuthUserId()), MealsUtil.DEFAULT_CALORIES_PER_DAY));
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