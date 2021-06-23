package com.tom.spring.jwt.security.service.impl;

import com.tom.spring.jwt.config.TokenProvider;
import com.tom.spring.jwt.security.authentication.AuthenticationFacade;
import com.tom.spring.jwt.security.dto.response.TokenDto;
import com.tom.spring.jwt.security.entity.Token;
import com.tom.spring.jwt.security.entity.User;
import com.tom.spring.jwt.security.repository.TokenRepository;
import com.tom.spring.jwt.security.repository.UserRepository;
import com.tom.spring.jwt.security.service.TokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;


@Service
public class TokenServiceImpl implements TokenService {

    private Long refreshTokenDuration;
    private TokenRepository tokenRepository;
    private UserRepository userRepository;
    private TokenProvider tokenProvider;
    private AuthenticationFacade authenticationFacade;

    public TokenServiceImpl(@Value("${jwt.refreshtoken.expiration}") Long refreshTokenDuration,
                            TokenRepository tokenRepository,
                            UserRepository userRepository,
                            TokenProvider tokenProvider,
                            AuthenticationFacade authenticationFacade){
        this.refreshTokenDuration = refreshTokenDuration;
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.authenticationFacade = authenticationFacade;
    }

    @Transactional
    public TokenDto createUpdatedToken(String accessToken,String refreshToken ){

        Token token =  tokenRepository.findByAccessTokenAndRefreshToken(accessToken
                                    ,refreshToken)
                                    .get(); //TO-DO: Not Found Token Exception

        verifyRefresh(token);

        String newAccessToken = tokenProvider.createToken(authenticationFacade.getAuthentication());
        String newRefreshToken = createRefreshToken();
        Instant newRefreshExpiryDate = Instant.now().plusMillis(refreshTokenDuration);

        token.updateToken(newAccessToken,newRefreshToken,newRefreshExpiryDate);

        token = tokenRepository.save(token);

        return new TokenDto(token.getAccessToken(),token.getRefreshToken());
    }

    @Override
    @Transactional
    public TokenDto createToken() {

        Authentication authentication = authenticationFacade.getAuthentication();
        Long authenticatedUserId = authenticationFacade.getSignedUser().get().getId();
        String accessToken = tokenProvider.createToken(authentication);

        User user = userRepository.findById(authenticatedUserId).get();
        Instant refreshExpiryDate = Instant.now().plusMillis(refreshTokenDuration);
        String refreshToken = createRefreshToken();

        Token token = new Token();
        token.setUser(user);
        token.updateToken(accessToken,refreshToken,refreshExpiryDate);

        token = tokenRepository.save(token);

        return new TokenDto(token.getAccessToken(),token.getRefreshToken());
    }

    private String createRefreshToken(){
        return  UUID.randomUUID().toString();
    }

    private void verifyRefresh(Token token) {

        Long tokenUserId = token.getUser().getId();
        Long authenticatedUserId = authenticationFacade.getSignedUser().get().getId();


        if (tokenUserId != authenticatedUserId){
            tokenRepository.deleteByUserId(authenticatedUserId);
            tokenRepository.deleteByUserId(tokenUserId);
            throw new IllegalStateException( "Refresh token should belong to the authenticator.");
        }

        if (token.getRefreshTokenExpiryDate().compareTo(Instant.now()) < 0) {
            tokenRepository.deleteByUserId(authenticatedUserId);
            throw new IllegalStateException( "Refresh token was expired. Please make a new signin request");
        }
    }




}
