package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Profile("hsqldb")
public class JdbcHsqlDbMealRepositoryImpl extends JdbcMealRepositoryImpl {

    @Autowired
    public JdbcHsqlDbMealRepositoryImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    public Meal save(Meal meal, int userId) {
        setDateTimeOrTimeStamp(Timestamp.valueOf(meal.getDateTime()));
        return super.save(meal, userId);
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return super.getBetweenWithConverting(Timestamp.valueOf(startDate), Timestamp.valueOf(endDate), userId);
    }
}