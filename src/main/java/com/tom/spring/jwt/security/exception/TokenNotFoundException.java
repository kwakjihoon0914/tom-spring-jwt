package com.tom.spring.jwt.security.exception;

public class TokenNotFoundException extends TokenException{

    public TokenNotFoundException(String message) {
        super(message);
    }
}
