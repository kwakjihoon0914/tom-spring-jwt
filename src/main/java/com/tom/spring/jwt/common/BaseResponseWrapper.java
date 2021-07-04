package com.tom.spring.jwt.common;


import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;

@Data
@Builder
public class BaseResponseWrapper<T> {

    int status;
    String message;
    String path;
    T content;

}
