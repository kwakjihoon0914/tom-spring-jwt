package com.tom.spring.jwt.test.security;

import com.tom.spring.jwt.common.ErrorResponse;
import com.tom.spring.jwt.config.JWTUtils;
import com.tom.spring.jwt.security.dto.response.TokenDto;
import com.tom.spring.jwt.security.entity.Token;
import com.tom.spring.jwt.security.repository.TokenRepository;
import com.tom.spring.jwt.security.repository.UserRepository;
import com.tom.spring.jwt.test.type.AcceptanceTest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class AuthenticationControllerTest extends AcceptanceTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    TokenRepository tokenRepository;


    @Test
    @Order(0)
    @DisplayName("올바른계정입력_로그인_JWT토큰응답")
    void 올바른계정입력_로그인_JWT토큰응답() {
        Map<String,String> params = new HashMap<>();

        params.put("username",DEFAULT_MANAGER_EMAIL);
        params.put("password",DEFAULT_MANAGER_PASSWORD);

        ExtractableResponse response =
                given()
                        .log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .port(RestAssured.port)

                        .body(params)

                        .when()
                        .post("/authenticate")

                        .then()
                        .log().all()
                        .extract();

        TokenDto actualToken = response.as(TokenDto.class);
        assertNotNull(actualToken);

        Token expected = tokenRepository.findByUserId(DEFAULT_MANAGER_ID).get();
        assertEquals(actualToken,TokenDto.of(expected));
    }

    @Test
    @Order(1)
    @DisplayName("올바른계정입력_중복로그인_업데이트토큰응답")
    void 올바른계정입력_중복로그인_업데이트토큰응답() {
        Map<String,String> params = new HashMap<>();

        params.put("username",DEFAULT_MANAGER_EMAIL);
        params.put("password",DEFAULT_MANAGER_PASSWORD);

        ExtractableResponse response =
                given()
                        .log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .port(RestAssured.port)

                        .body(params)

                        .when()
                        .post("/authenticate")

                        .then()
                        .log().all()
                        .extract();

        TokenDto actualTokenDto = response.as(TokenDto.class);

        String accessToken = response.header(JWTUtils.AUTHORIZATION_HEADER).substring(7);
        assertEquals(accessToken,actualTokenDto.getAccessToken());

        Token expected = tokenRepository.findByUserId(DEFAULT_MANAGER_ID).get();
        assertEquals(actualTokenDto,TokenDto.of(expected));
    }

    @Test
    @Order(2)
    @DisplayName("올바르지않는계정입력_로그인_에러응답")
    void 올바르지않는계정입력_로그인_에러응답() {
        Map<String,String> params = new HashMap<>();

        params.put("username",DEFAULT_MANAGER_EMAIL);
        params.put("password","1234");

        ExtractableResponse response =
                given()
                        .log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .port(RestAssured.port)

                        .body(params)

                        .when()
                        .post("/authenticate")

                        .then()
                        .log().all()
                        .extract();
        ErrorResponse actualErrorResponse = response.as(ErrorResponse.class);
        ErrorResponse expectedErrorResponse = new ErrorResponse();
        expectedErrorResponse.setStatus(401);

        assertEquals(actualErrorResponse.getStatus(),expectedErrorResponse.getStatus());


    }




    @Test
    void refreshToken() {


    }
}