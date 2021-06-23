package com.tom.spring.jwt.security.service.impl;

import com.tom.spring.jwt.config.TokenProvider;
import com.tom.spring.jwt.security.authentication.AuthenticationFacade;
import com.tom.spring.jwt.security.dto.request.TokenRefreshDto;
import com.tom.spring.jwt.security.dto.response.TokenDto;
import com.tom.spring.jwt.security.entity.RefreshToken;
import com.tom.spring.jwt.security.entity.User;
import com.tom.spring.jwt.security.repository.RefreshTokenRepository;
import com.tom.spring.jwt.security.repository.UserRepository;
import com.tom.spring.jwt.security.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;


@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private Long refreshTokenDuration;
    private RefreshTokenRepository refreshTokenRepository;
    private UserRepository userRepository;
    private TokenProvider tokenProvider;
    private AuthenticationFacade authenticationFacade;

    public RefreshTokenServiceImpl( @Value("${jwt.refreshtoken.expiration}") Long refreshTokenDuration,
                                    RefreshTokenRepository refreshTokenRepository,
                                    UserRepository userRepository,
                                    TokenProvider tokenProvider,
                                    AuthenticationFacade authenticationFacade){
        this.refreshTokenDuration = refreshTokenDuration;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.authenticationFacade = authenticationFacade;
    }

    @Transactional
    public TokenDto createNewToken(String token){
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token).get();

        verifyExpiration(refreshToken);

        return null;
    }

    @Override
    @Transactional
    public RefreshToken createRefreshToken(Long userId) {

        User user = userRepository.findById(userId).get();
        Instant expiryDate = Instant.now().plusMillis(refreshTokenDuration);
        String token = UUID.randomUUID().toString();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(expiryDate);
        refreshToken.setToken(token);

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    private RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new IllegalStateException( "Refresh token was expired. Please make a new signin request");
        }
        return token;
    }




}
