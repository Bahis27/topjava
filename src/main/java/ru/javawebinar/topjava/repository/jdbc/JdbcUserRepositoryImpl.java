package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
            insertRolesIntoDb(user);
            return user;
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE users SET name=:name, email=:email, password=:password, " +
                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
            return null;
        }
        updateRolesIntoDb(user);
        return user;
    }

    private void updateRolesIntoDb(User user) {
        jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.getId());
        insertRolesIntoDb(user);
    }

    private void insertRolesIntoDb(User user) {
        List<Role> roles = new ArrayList<>(user.getRoles());
        jdbcTemplate.batchUpdate("INSERT INTO user_roles (user_id, role) VALUES (?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Role role = roles.get(i);
                ps.setInt(1, user.getId());
                ps.setString(2, role.name());
            }
            @Override
            public int getBatchSize() {
                return roles.size();
            }
        });
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        boolean isDeleted = jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
        if (isDeleted) {
            jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", id);
        }
        return isDeleted;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        return setRolesToUser(users, id);
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        if (users.isEmpty()) {
            return null;
        }
        return setRolesToUser(users, DataAccessUtils.singleResult(users).getId());
    }

    @Override
    public List<User> getAll() {
        List<User> users = jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);
        ResultSetExtractor<Map<Integer, Set<Role>>> rse = rs -> {
            Map<Integer, Set<Role>> roles = new LinkedHashMap<>();
            while (rs.next()) {
                roles.merge(rs.getInt("user_id"),
                        Stream.of(Enum.valueOf(Role.class, rs.getString("role"))).collect(Collectors.toSet()),
                        (oldV, newV) -> {
                            oldV.addAll(newV);
                            return oldV;
                        });
            }
            return roles;
        };
        Map<Integer, Set<Role>> roles = jdbcTemplate.query("SELECT * FROM user_roles", rse);
        users.forEach(user -> user.setRoles(roles.get(user.getId())));
        return users;
    }

    private User setRolesToUser(List<User> users, int id) {
        return setRolesToUser(DataAccessUtils.singleResult(users), id);
    }

    private User setRolesToUser(User user, int id) {
        if (user == null) {
            return null;
        }
        List<Role> roles = jdbcTemplate.query("SELECT role FROM user_roles WHERE user_id=?", ROLE_ROW_MAPPER, id);
        user.setRoles(new LinkedHashSet<>(roles));
        return user;
    }
}
