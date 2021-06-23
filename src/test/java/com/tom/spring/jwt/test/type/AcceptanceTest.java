package com.tom.spring.jwt.test.type;

import com.tom.spring.jwt.security.entity.User;
import com.tom.spring.jwt.security.repository.UserRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {

    private static final String DEFAULT_MANAGER_ACCOUNT_EMAIL = "admin@admin.com";
    private static final String DEFAULT_MANAGER_ACCOUNT_PASSWORD = "admin";

    //@LocalServerPort
    //int port;

    @BeforeEach
    public void setUp() {
        if (RestAssured.port == RestAssured.UNDEFINED_PORT) {
            RestAssured.port = 8080;
        }
    }



}