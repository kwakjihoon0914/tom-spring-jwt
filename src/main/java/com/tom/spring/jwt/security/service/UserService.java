package com.tom.spring.jwt.security.service;

import com.tom.spring.jwt.security.config.AuthorityType;
import com.tom.spring.jwt.security.entity.User;

import java.util.Collection;

public interface UserService {

    User getUserWithAuthorities();
    User getCurrentUser();

    Collection<User> getUsersByRole(AuthorityType authorityType);
}
