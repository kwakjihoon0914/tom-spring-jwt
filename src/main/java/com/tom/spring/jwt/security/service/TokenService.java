package com.tom.spring.jwt.security.service;

import com.tom.spring.jwt.security.dto.response.TokenDto;
public interface TokenService {

    TokenDto createToken();
    TokenDto createUpdatedToken(String accessToken,String refreshToken );
}
