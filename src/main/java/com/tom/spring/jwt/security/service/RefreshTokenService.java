package com.tom.spring.jwt.security.service;

import com.tom.spring.jwt.security.entity.RefreshToken;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(Long userId);
}
