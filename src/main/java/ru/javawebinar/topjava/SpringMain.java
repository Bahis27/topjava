package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.inmemory.InMemoryUserRepositoryImpl;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.util.Arrays;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            User u1 = adminUserController.create(new User(null, "admin", "admin@gmail.com", "apass", Role.ROLE_ADMIN));
            User u2 = adminUserController.create(new User(null, "User", "user1@gmail.com", "u1pass", Role.ROLE_USER));
            InMemoryUserRepositoryImpl inMemoryUserRepository = appCtx.getBean(InMemoryUserRepositoryImpl.class);
            inMemoryUserRepository.getAll().forEach(System.out::println);
        }
    }
}