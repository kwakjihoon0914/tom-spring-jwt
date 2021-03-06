package com.tom.spring.jwt.security.service.impl;

import com.tom.spring.jwt.config.TokenProvider;
import com.tom.spring.jwt.security.authentication.AuthenticationFacade;
import com.tom.spring.jwt.security.dto.response.TokenDto;
import com.tom.spring.jwt.security.entity.Token;
import com.tom.spring.jwt.security.entity.User;
import com.tom.spring.jwt.security.exception.TokenException;
import com.tom.spring.jwt.security.exception.TokenExpiredException;
import com.tom.spring.jwt.security.exception.TokenNotFoundException;
import com.tom.spring.jwt.security.repository.TokenRepository;
import com.tom.spring.jwt.security.repository.UserRepository;
import com.tom.spring.jwt.security.service.TokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;


@Service
public class TokenServiceImpl implements TokenService {

    final private Long refreshTokenDuration;
    final private TokenRepository tokenRepository;
    final private UserRepository userRepository;
    final private TokenProvider tokenProvider;
    final private AuthenticationFacade authenticationFacade;

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
    @Override
    public TokenDto createUpdatedToken(String accessToken,String refreshToken ){

        Token token =  tokenRepository.findByAccessTokenAndRefreshToken(accessToken
                                    ,refreshToken)
                                    .get(); //TO-DO: Not Found Token Exception

        verifyRefresh(token);

        String newAccessToken = tokenProvider.createToken(authenticationFacade.getAuthentication());
        String newRefreshToken = createRefreshToken();
        LocalDateTime newRefreshExpiryDate = LocalDateTime.now().plusSeconds(refreshTokenDuration);

        token.updateToken(newAccessToken,newRefreshToken,newRefreshExpiryDate);

        token = tokenRepository.save(token);

        return TokenDto.of(token);
    }

    @Override
    @Transactional
    public TokenDto createToken() {

        Authentication authentication = authenticationFacade.getAuthentication();
        Long authenticatedUserId = authenticationFacade.getAuthenticatedUser().get().getId();
        String accessToken = tokenProvider.createToken(authentication);

        User user = userRepository.findById(authenticatedUserId).get();
        LocalDateTime refreshExpiryDate = LocalDateTime.now().plusSeconds(refreshTokenDuration);

        String refreshToken = createRefreshToken();

        Optional<Token> tokenOptional  =tokenRepository.findByUserId(user.getId());

        Token token;
        if (tokenOptional.isPresent()){
            token = tokenOptional.get();
        }else{
            token = new Token();
            token.setUser(user);
        }
        token.updateToken(accessToken,refreshToken,refreshExpiryDate);
        token = tokenRepository.save(token);

        return TokenDto.of(token);
    }

    private String createRefreshToken(){
        return  UUID.randomUUID().toString();
    }

    private void verifyRefresh(Token token) {

        Long tokenUserId = token.getUser().getId();
        Long authenticatedUserId = authenticationFacade.getAuthenticatedUser().get().getId();

        if (tokenUserId != authenticatedUserId){
            tokenRepository.deleteByUserId(authenticatedUserId);
            tokenRepository.deleteByUserId(tokenUserId);
            throw new TokenException( "Refresh token should belong to the authenticator.");
        }

        if (token.getRefreshTokenExpiryDate().compareTo(LocalDateTime.now()) < 0) {
            tokenRepository.deleteByUserId(authenticatedUserId);
            throw new TokenExpiredException( "Refresh token was expired. Please make a new signin request");
        }
    }




}
