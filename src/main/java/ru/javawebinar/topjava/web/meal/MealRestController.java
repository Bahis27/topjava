package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import java.util.List;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public List<Meal> getAll() {
        log.info("getAll");
        return service.getAll();
    }

    public Meal get(int id, int userId) {
        log.info("get {}", id, userId);
        return service.get(id, userId);
    }

    public Meal create(Meal meal, int userId) {
        log.info("create {}", meal, userId);
        return service.create(meal, userId);
    }

    public boolean delete(int id, int userId) {
        log.info("delete {}", id, userId);
        return service.delete(id, userId);
    }

    public Meal update(Meal meal, int id, int userId) {
        log.info("update {}", meal, id, userId);
        return service.update(meal, userId);
    }

}