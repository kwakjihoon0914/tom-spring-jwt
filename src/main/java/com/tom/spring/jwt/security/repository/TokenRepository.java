package com.tom.spring.jwt.security.repository;

import com.tom.spring.jwt.security.entity.Authority;
import com.tom.spring.jwt.security.entity.Token;
import com.tom.spring.jwt.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, String> {

    Optional<Token> findByAccessTokenAndRefreshToken(String token,String refreshToken);

    @Modifying
    Long deleteByUserId(Long id);

}
