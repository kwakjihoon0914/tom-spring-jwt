package com.tom.spring.jwt.security.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JWTTokenDto {

    private String token;

}
