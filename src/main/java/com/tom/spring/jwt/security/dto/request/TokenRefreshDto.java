package com.tom.spring.jwt.security.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TokenRefreshDto {

    @NotBlank
    private String refreshToken;


}
