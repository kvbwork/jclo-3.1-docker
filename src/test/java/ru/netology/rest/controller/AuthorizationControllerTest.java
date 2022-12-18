package ru.netology.rest.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.netology.rest.enums.Authorities;
import ru.netology.rest.repository.UserRepository;

import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.netology.rest.enums.Authorities.*;

@SpringBootTest
class AuthorizationControllerTest {

    private static final String AUTHORIZE_PATH = "/authorize";
    private static final String USER_PARAM = "user";
    private static final String PASSWORD_PARAM = "password";

    private static final String TEST_USER_NAME = "testuser";
    private static final String TEST_USER_PASSWORD = "testpassword";
    private static final List<Authorities> TEST_USER_AUTHORITIES = List.of(READ, WRITE, DELETE);

    @TestConfiguration
    public static class TestConfig {
        @Bean
        @Primary
        UserRepository userRepositoryTest() {
            var repo = new UserRepository();
            repo.save(TEST_USER_NAME, TEST_USER_PASSWORD, TEST_USER_AUTHORITIES);
            return repo;
        }
    }

    @Autowired
    WebApplicationContext context;
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @AfterEach
    void tearDown() {
        mockMvc = null;
    }

    @Test
    void authorize_no_param_failed() throws Exception {
        mockMvc.perform(get(AUTHORIZE_PATH))
                .andExpect(status().isBadRequest());
    }

    @Test
    void authorize_empty_param_failed() throws Exception {
        mockMvc.perform(get(AUTHORIZE_PATH)
                .queryParam(USER_PARAM, "")
                .queryParam(PASSWORD_PARAM, "")
        ).andExpect(status().isBadRequest());
    }

    @Test
    void authorize_user_password_failed() throws Exception {
        mockMvc.perform(get(AUTHORIZE_PATH)
                .queryParam(USER_PARAM, "nonexistuser")
                .queryParam(PASSWORD_PARAM, "nonexistpassword")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    void authorize_user_password_success() throws Exception {
        mockMvc.perform(get(AUTHORIZE_PATH)
                .queryParam(USER_PARAM, TEST_USER_NAME)
                .queryParam(PASSWORD_PARAM, TEST_USER_PASSWORD)
        ).andExpect(status().isOk());
    }

    @Test
    void authorize_200_return_json_success() throws Exception {
        final var expectedResult = TEST_USER_AUTHORITIES.stream()
                .map(Authorities::name)
                .toArray(String[]::new);

        mockMvc.perform(get(AUTHORIZE_PATH)
                .queryParam(USER_PARAM, TEST_USER_NAME)
                .queryParam(PASSWORD_PARAM, TEST_USER_PASSWORD)
        ).andExpectAll(
                status().isOk(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                jsonPath("$").value(contains(expectedResult))
        );
    }

}