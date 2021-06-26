package com.tom.spring.jwt.security.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class AuthenticationFacadeImpl implements AuthenticationFacade {
    @Override
    public Optional<AuthenticatedUser> getAuthenticatedUser(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) return Optional.empty();

        AuthenticatedUser currentAuthenticatedUser = (AuthenticatedUser) authentication.getPrincipal();
        return Optional.ofNullable(currentAuthenticatedUser);
    }

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
