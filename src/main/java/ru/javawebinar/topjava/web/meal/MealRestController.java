package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<MealTo> getAll() {
        log.info("getAll");
        return MealsUtil.getWithExcess(service.getAll(SecurityUtil.getAuthUserId()), MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public List<MealTo> getAllFiltered(String sStartDate, String sEndDate, String sStartTime, String sEndTime) {
        log.info("getAllFiltered");
        LocalDate startDate = (sStartDate.length() == 0) ? LocalDate.MIN : LocalDate.parse(sStartDate);
        LocalDate endDate = (sEndDate.length() == 0) ? LocalDate.MAX : LocalDate.parse(sEndDate);
        LocalTime startTime = (sStartTime.length() == 0) ? LocalTime.MIN : LocalTime.parse(sStartTime);
        LocalTime endTime = (sEndTime.length() == 0) ? LocalTime.MAX : LocalTime.parse(sEndTime);
        if (startTime.isAfter(endTime))
            endTime = LocalTime.MAX;
        if (startDate.isAfter(endDate))
            endDate = LocalDate.MAX;

        List<Meal> meals = MealsUtil.getFiltered(service.getAll(SecurityUtil.getAuthUserId()), startDate, endDate);
        return MealsUtil.getFilteredWithExcess(meals, MealsUtil.DEFAULT_CALORIES_PER_DAY, startTime, endTime);
    }

    public Meal get(int id) {
        log.info("get {}", id);
        return service.get(id, SecurityUtil.getAuthUserId());
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        return service.create(meal, SecurityUtil.getAuthUserId());
    }

    public boolean delete(int id) {
        log.info("delete {}", id);
        return service.delete(id, SecurityUtil.getAuthUserId());
    }

    public Meal update(Meal meal, int id) {
        log.info("update {}", meal, id);
        assureIdConsistent(meal, id);
        return service.update(meal, SecurityUtil.getAuthUserId());
    }

}