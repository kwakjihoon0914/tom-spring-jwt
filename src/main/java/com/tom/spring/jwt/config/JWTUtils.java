package com.tom.spring.jwt.config;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class JWTUtils {

    public final static String AUTHORIZATION_HEADER = "Authorization";
    public final static String AUTHORIZATION_TOKEN_PREFIX = "Bearer ";

    public static String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(AUTHORIZATION_TOKEN_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
