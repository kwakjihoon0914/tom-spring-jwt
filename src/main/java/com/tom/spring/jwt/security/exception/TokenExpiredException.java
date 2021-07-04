package com.tom.spring.jwt.security.exception;

public class TokenExpiredException extends TokenException{
    public TokenExpiredException(String message) {
        super(message);
    }
}
