package com.tom.spring.jwt.test.security;

import com.tom.spring.jwt.security.dto.response.TokenDto;
import com.tom.spring.jwt.test.type.AcceptanceTest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class SecurityAcceptanceTest extends AcceptanceTest {

    @Test
    void 로그인(){

        Map<String,String> params = new HashMap<>();

        params.put("username","test@admin.com");
        params.put("password","admin");

        ExtractableResponse response =
                given()
                        .baseUri("http://localhost")
                        .log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .port(RestAssured.port)
                        .when().post("/authenticate")

                        .then()
                        .extract();

        TokenDto tokenDto = response.as(TokenDto.class);
        //assertThat(response.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.code());


    }

}
