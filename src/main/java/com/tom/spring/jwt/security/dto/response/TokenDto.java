package com.tom.spring.jwt.security.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TokenDto {

    private String token;
    private String refreshToken;

    public TokenDto(String token,String refreshToken){
        this.token = token;
        this.refreshToken = refreshToken;
    }

}
