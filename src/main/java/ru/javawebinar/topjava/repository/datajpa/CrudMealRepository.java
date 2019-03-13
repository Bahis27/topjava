package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    @Override
    @Transactional
    Meal save(Meal meal);

    @Transactional
    Integer deleteByIdAndUserId(Integer id, Integer userId);

    Optional<Meal> findByIdAndUserId(Integer id, Integer userId);

    List<Meal> findAllByUserIdOrderByDateTimeDesc(Integer userId);

    List<Meal> findAllByUserIdAndDateTimeBetweenOrderByDateTimeDesc(Integer userId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT m FROM Meal m LEFT JOIN FETCH m.user WHERE m.id=:id")
    Meal getMeaByMealIdWithUser(@Param("id") int id);
}
