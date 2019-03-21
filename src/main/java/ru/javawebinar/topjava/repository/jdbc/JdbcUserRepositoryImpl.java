package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);
    private static final RowMapper<Role> ROLE_ROW_MAPPER = (rs, rowNum) -> Enum.valueOf(Role.class, rs.getString("role"));

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepositoryImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE users SET name=:name, email=:email, password=:password, " +
                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
            return null;
        }
        List<Role> roles = jdbcTemplate.query("SELECT role FROM user_roles WHERE user_id=?", ROLE_ROW_MAPPER, user.getId());
        user.setRoles(new LinkedHashSet<>(roles));
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    @Transactional
    public User get(int id) {
        List<Role> roles = jdbcTemplate.query("SELECT role FROM user_roles WHERE user_id=?", ROLE_ROW_MAPPER, id);
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        User user = DataAccessUtils.singleResult(users);
        if (user == null) {
            return null;
        }
        user.setRoles(new LinkedHashSet<>(roles));
        return user;
    }

    @Override
    @Transactional
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        User user = DataAccessUtils.singleResult(users);
        if (user == null) {
            return null;
        }
        List<Role> roles = jdbcTemplate.query("SELECT role FROM user_roles WHERE user_id=?", ROLE_ROW_MAPPER, user.getId());
        user.setRoles(new LinkedHashSet<>(roles));
        return user;
    }

    @Override
    @Transactional
    public List<User> getAll() {
        List<User> users = jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);
        ResultSetExtractor<Map<Integer, List<Role>>> rse = rs -> {
            Map<Integer, List<Role>> roles = new LinkedHashMap<>();
            while (rs.next()) {
                roles.merge(rs.getInt("user_id"), Collections.singletonList(Enum.valueOf(Role.class, rs.getString("role"))), (oldV, newV) -> {ArrayList<Role> list = new ArrayList<>();
                list.addAll(oldV);
                list.addAll(newV);
                return list;});
            }
            return roles;
        };
        Map<Integer, List<Role>> roles = jdbcTemplate.query("SELECT * FROM user_roles", rse);
        if (users == null || roles == null){
            return null;
        }
        users.forEach(user -> user.setRoles(roles.get(user.getId())));
        return users;
    }
}
