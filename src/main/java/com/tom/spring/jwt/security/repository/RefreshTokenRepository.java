package com.tom.spring.jwt.security.repository;

import com.tom.spring.jwt.security.entity.Authority;
import com.tom.spring.jwt.security.entity.RefreshToken;
import com.tom.spring.jwt.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    Optional<RefreshToken> findByToken(String token);

    @Modifying
    Long deleteByUserId(Long id);

}
