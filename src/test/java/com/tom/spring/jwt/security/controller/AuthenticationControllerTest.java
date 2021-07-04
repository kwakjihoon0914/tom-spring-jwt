package com.tom.spring.jwt.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tom.spring.jwt.config.TokenProvider;
import com.tom.spring.jwt.security.dto.request.LoginDto;
import com.tom.spring.jwt.security.dto.response.TokenDto;
import com.tom.spring.jwt.security.entity.Token;
import com.tom.spring.jwt.security.repository.TokenRepository;
import com.tom.spring.jwt.security.service.TokenService;
import com.tom.spring.jwt.test.utils.TestConstants;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.security.Key;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenRepository tokenRepository;

    @Test
    @Order(0)
    @DisplayName("올바른 계정입력시 Token 응답")
    public void authorize_success() throws Exception {

        /*****************************************************************/
        /* 1) Define Parameter */
        /*****************************************************************/
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername(TestConstants.MANAGER_EMAIL);
        loginDto.setPassword(TestConstants.MANAGER_PASSWORD);

        String body =  objectMapper.writeValueAsString(loginDto);

        /*****************************************************************/
        /* 2) Mock Test */
        /*****************************************************************/
        MvcResult mvcResult =  this.mockMvc.perform(
                        post("/authenticate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(body))
                .andDo(print())
                .andReturn();

        /*****************************************************************/
        /* 3) Assert Test */
        /*****************************************************************/
        TokenDto actual =  objectMapper.readValue(mvcResult.getResponse().getContentAsString(), TokenDto.class);
        Token token = tokenRepository.findByUserId(TestConstants.MANAGER_ID).get();

        assertEquals(actual.getAccessToken(),token.getAccessToken());
        assertEquals(actual.getRefreshToken(),token.getRefreshToken());

    }

    @Test
    @Order(1)
    @DisplayName("잘못된 계정입력시 자격증명 에러응답")
    public void authorize_notFoundedUser_fail() throws Exception {

        /*****************************************************************/
        /* 1) Define Parameter */
        /*****************************************************************/
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername(TestConstants.NOT_FOUNDED_USER_EMAIL);
        loginDto.setPassword(TestConstants.NOT_FOUNDED_USER_PASSWORD);

        String body =  objectMapper.writeValueAsString(loginDto);
        /*****************************************************************/
        /* 2) Mock Test */
        /*****************************************************************/
        MvcResult mvcResult =  this.mockMvc.perform(
                post("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andReturn();

        /*****************************************************************/
        /* 3) Assert Test */
        /*****************************************************************/
        int actualStatus =  mvcResult.getResponse().getStatus();
        int expectedStatus = 401;

        assertEquals(actualStatus,expectedStatus);

    }


    @Test
    @Order(2)
    @DisplayName("잘못된 Password 입력시 자격증명 에러응답")
    public void authorize_badCredential_fail() throws Exception {

        /*****************************************************************/
        /* 1) Define Parameter */
        /*****************************************************************/
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername(TestConstants.USER_EMAIL);
        loginDto.setPassword(TestConstants.NOT_FOUNDED_USER_PASSWORD);

        String body =  objectMapper.writeValueAsString(loginDto);
        /*****************************************************************/
        /* 2) Mock Test */
        /*****************************************************************/
        MvcResult mvcResult =  this.mockMvc.perform(
                post("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andReturn();

        /*****************************************************************/
        /* 3) Assert Test */
        /*****************************************************************/
        int actualStatus =  mvcResult.getResponse().getStatus();
        int expectedStatus = 401;

        assertEquals(actualStatus,expectedStatus);

    }



    @Test
    void refreshToken() {
    }
}