package com.tom.spring.jwt.security.authentication;

import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface AuthenticationFacade {
    Optional<AuthenticatedUser> getAuthenticatedUser();
    Authentication getAuthentication();

}
