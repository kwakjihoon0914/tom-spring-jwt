package com.tom.spring.jwt.security.exception;

public class TokenException extends RuntimeException {

    public TokenException(String message){
        super(message);
    }
}
