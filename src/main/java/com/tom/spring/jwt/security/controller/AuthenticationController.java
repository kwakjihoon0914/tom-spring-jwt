package com.tom.spring.jwt.security.controller;


import com.tom.spring.jwt.config.JWTFilter;
import com.tom.spring.jwt.config.TokenProvider;
import com.tom.spring.jwt.security.authentication.AuthenticationFacade;
import com.tom.spring.jwt.security.dto.request.TokenRefreshDto;
import com.tom.spring.jwt.security.dto.response.TokenDto;
import com.tom.spring.jwt.security.dto.request.LoginDto;
import com.tom.spring.jwt.security.service.TokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@AllArgsConstructor
@RestController
public class AuthenticationController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenService tokenService;

    @PostMapping("/authenticate")
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);


        TokenDto tokenDto = tokenService.createToken();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, JWTFilter.AUTHORIZATION_TOKEN_PREFIX + tokenDto.getAccessToken());

        return ResponseEntity.ok()
                        .headers(httpHeaders)
                        .body(tokenDto);
    }


    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshDto tokenRefreshDto, HttpServletRequest request) {
        String accessToken =  JWTFilter.resolveToken(request);
        String refreshToken = tokenRefreshDto.getRefreshToken();

        TokenDto tokenDto = tokenService.createUpdatedToken(accessToken,refreshToken);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, JWTFilter.AUTHORIZATION_TOKEN_PREFIX + tokenDto.getAccessToken());

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(tokenDto);
    }

}
