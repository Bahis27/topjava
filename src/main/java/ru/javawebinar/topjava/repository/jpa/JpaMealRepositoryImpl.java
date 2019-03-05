package ru.javawebinar.topjava.repository.jpa;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepositoryImpl implements MealRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        User user = em.find(User.class, userId);
        meal.setUser(user);
        if (meal.isNew()) {
            em.persist(meal);
            return meal;
        } else {
            if (get(meal.getId(), userId) == null) {
                return null;
            }
            return em.merge(meal);
        }
    }

    @Override
    public Meal get(int id, int userId) {
        List<Meal> meals = em.createNamedQuery(Meal.BY_ID, Meal.class)
                .setParameter("id", id)
                .setParameter("userid", userId)
                .getResultList();
        return DataAccessUtils.singleResult(meals);
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        if (get(id, userId) == null) {
            return false;
        } else {
         return em.createNamedQuery(Meal.DELETE)
                 .setParameter("id", id)
                 .executeUpdate() != 0;
        }
    }

    @Override
    public List<Meal> getAll(int userId) {
        return em.createNamedQuery(Meal.ALL_SORTED, Meal.class)
                .setParameter("userid", userId)
                .getResultList();
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return em.createNamedQuery(Meal.ALL_BETWEEN, Meal.class)
                .setParameter("startdate", startDate)
                .setParameter("enddate", endDate)
                .setParameter("userid", userId)
                .getResultList();
    }
}