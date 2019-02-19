package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

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

    public List<Meal> getAll(int authUserId) {
        log.info("getAll");
        return service.getAll(authUserId);
    }

    public Meal get(int id, int authUserId) {
        log.info("get {}", id, authUserId);
        return service.get(id, authUserId);
    }

    public Meal create(Meal meal, int authUserId) {
        log.info("create {}", meal, authUserId);
        checkNew(meal);
        return service.create(meal, authUserId);
    }

    public boolean delete(int id, int authUserId) {
        log.info("delete {}", id, authUserId);
        return service.delete(id, authUserId);
    }

    public Meal update(Meal meal, int authUserId, int id) {
        log.info("update {}", meal, authUserId, id);
        assureIdConsistent(meal, id);
        return service.update(meal, authUserId);
    }

}