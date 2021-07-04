package com.tom.spring.jwt.security.dto.response;

import com.tom.spring.jwt.security.entity.Token;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TokenDto {

    private String accessToken;
    private String refreshToken;
    private String accessTokenType;
    private LocalDateTime refreshTokenExpiryDate;

    public TokenDto(String accessToken, String refreshToken, LocalDateTime refreshTokenExpiryDate){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenType = "Bearer";
        this.refreshTokenExpiryDate = refreshTokenExpiryDate;
    }

    public static TokenDto of(Token token){
        return  new TokenDto(token.getAccessToken(),token.getRefreshToken(),token.getRefreshTokenExpiryDate());
    }
}
