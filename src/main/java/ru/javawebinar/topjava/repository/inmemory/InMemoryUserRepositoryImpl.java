package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

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
    private AtomicInteger counter = new AtomicInteger(4);
    private static final User NOT_EXIST_USER = new User(0, "not exist", "not exist", "not exist", Role.ROLE_USER);

    public InMemoryUserRepositoryImpl() {
        repository.put(1, new User(1, "Petya", "Petya@gmail.com", "ppass", Role.ROLE_USER));
        repository.put(2, new User(2, "Vasya", "Vasya@gmail.com", "vpass", Role.ROLE_USER));
        repository.put(3, new User(3, "OneMoreUser", "OMU@gmail.com", "opass", Role.ROLE_USER));
        repository.put(4, new User(4, "User", "User@gmail.com", "upass", Role.ROLE_USER));
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