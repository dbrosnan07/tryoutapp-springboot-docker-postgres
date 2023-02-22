package com.example.tryoutapp;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.lifecycle.Startables;
import com.example.tryoutapp.model.User;
import java.util.Map;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = UserTests.Initializer.class)
class UserTests 
{
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    @Order(1)
    void getUsers() {
        ResponseEntity<User[]> responseEntity = restTemplate.getForEntity("/user/all", User[].class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(0, responseEntity.getBody().length);
    }

    @Test
    @Order(2)
    void createFirstUser() {
        User user = new User();
        user.setId(1);
        user.setEmail("user1@gmail.com");
        user.setName("User1");
        ResponseEntity<User> responseEntity = restTemplate.postForEntity("/user/{" + String.valueOf(user.getId()) + "}", user, User.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        user = responseEntity.getBody();
        assertNotNull(user);
    }

    @Test
    @Order(3)
    void checkUsers() {
        ResponseEntity<User[]> responseEntity = restTemplate.getForEntity("/user/all", User[].class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1, responseEntity.getBody().length);
    }

    @Test
    @Order(4)
    void createSecondUser() {
        User user = new User();
        user.setId(2);
        user.setEmail("user2@gmail.com");
        user.setName("User2");
        ResponseEntity<User> responseEntity = restTemplate.postForEntity("/user/{" + String.valueOf(user.getId()) + "}", user, User.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        user = responseEntity.getBody();
        assertNotNull(user);
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres");
        private static void startContainers() {
            Startables.deepStart(Stream.of(postgres)).join();
        }
        private static Map<String, String> createContextConfiguration() {
            return Map.of(
                    "spring.datasource.url", postgres.getJdbcUrl(),
                    "spring.datasource.username", postgres.getDatabaseName(),
                    "spring.datasource.password", postgres.getPassword()
            );
        }
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            startContainers();
            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            MapPropertySource testContainers = new MapPropertySource("testcontainers", (Map) createContextConfiguration());
            environment.getPropertySources().addFirst(testContainers);
        }
    }

}
