package ru.javawebinar.topjava.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.web.AbstractControllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.UserTestData.*;

class AdminUIControllerTest extends AbstractControllerTest {

    private static final String REST_URL = "/ajax/admin/users";

    @Test
    void testSetEnabled() throws Exception {
        User user = new User(USER);
        user.setEnabled(true);

        mockMvc.perform(post(REST_URL + "/enable?id=100000&enabled=true")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertMatch(userService.getAll(), ADMIN, user);
    }
}
