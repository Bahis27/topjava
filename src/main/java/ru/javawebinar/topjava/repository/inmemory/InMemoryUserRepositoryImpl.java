package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepositoryImpl.class);
    private Map<Integer, User> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    public InMemoryUserRepositoryImpl() {
        Arrays.asList(new User(null, "Petya", "Petya@gmail.com", "ppass", Role.ROLE_USER),
        new User(null, "Vasya", "Vasya@gmail.com", "vpass", Role.ROLE_USER),
        new User(null, "OneMoreUser", "OMU@gmail.com", "opass", Role.ROLE_USER),
        new User(null, "User", "User@gmail.com", "upass", Role.ROLE_USER),
        new User(null, "anna", "anna@gmail.com", "apass", Role.ROLE_USER, Role.ROLE_ADMIN),
        new User(null, "OneMoreUser", "OneMU@gmail.com", "omupass", Role.ROLE_USER))
                .forEach(this::save);
    }

    @Override
    public User save(User user) {
        log.info("save {}", user);
        if (user.isNew()) {
            user.setId(counter.incrementAndGet());
            repository.put(user.getId(), user);
            return user;
        }
        return repository.computeIfPresent(user.getId(), (id, oldUser) -> user);
    }

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return repository.remove(id) != null;
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        return repository.values().stream()
                .sorted(Comparator.comparing((User user) -> user.getName().toLowerCase()).thenComparingInt(User::getId))
                .collect(Collectors.toList());
    }

    @Override
    public User getByEmail(String email) {
        return repository.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findAny().orElse(null);
    }
}