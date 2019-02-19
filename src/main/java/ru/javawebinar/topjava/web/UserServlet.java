package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.service.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class UserServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);

    private ConfigurableApplicationContext applicationContext;
    private UserService service;

    @Override
    public void init() throws ServletException {
        super.init();
        applicationContext = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        service = applicationContext.getBean(UserServiceImpl.class);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("forward to users");
        request.setAttribute("users", service.getAll());
        request.getRequestDispatcher("/users.jsp").forward(request, response);
    }
}
