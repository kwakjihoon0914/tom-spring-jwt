package com.tom.spring.jwt.security.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TokenDto {

    private String accessToken;
    private String refreshToken;

    public TokenDto(String accessToken,String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }


    public static TokenDto of(String accessToken,String refreshToken){
        return  new TokenDto(accessToken,refreshToken);
    }
}
