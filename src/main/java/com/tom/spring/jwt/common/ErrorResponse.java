package com.tom.spring.jwt.common;


import lombok.Data;

import java.time.Instant;

@Data
public class ErrorResponse {

    Instant timestamp;
    int status;
    String error;
    String path;
}
