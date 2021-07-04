package com.tom.spring.jwt.common;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;

@ControllerAdvice
@AllArgsConstructor
public class BaseResponseAdvice implements ResponseBodyAdvice<Object> {

    private static final String CONVERT_NAME = "org.springframework.http.converter.StringHttpMessageConverter";//String message converter

    private final ObjectMapper objectMapper;


    private boolean isResultUnite(MethodParameter returnType, Class aClass) {
        Method method = returnType.getMethod();

        return aClass.isAnnotationPresent(ResultUnite.class) ||method.isAnnotationPresent(ResultUnite.class);//Whether it is annotated by @ResultUnite
    }

    private boolean isResponseEntity(MethodParameter returnType){
        return returnType.getNestedParameterType().equals(ResponseEntity.class);
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {

        //returnType.getContainingClass()
        // => Controller

        //returnType.getNestedParameterType()
        // => ResponseEntity

        //returnType.getNestedGenericParameterType()
        // => Dto

        return isResponseEntity(returnType) && isResultUnite(returnType, converterType);
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        int status = HttpStatus.OK.value();
        if (response instanceof ServletServerHttpResponse) {
            status = ((ServletServerHttpResponse) response).getServletResponse().getStatus();
        }
        final String path = request.getURI().getPath();

        BaseResponseWrapper wrapper = BaseResponseWrapper.builder()
                .status(status)
                .path(path)
                .content(body).build();

        if (CONVERT_NAME.equalsIgnoreCase(selectedConverterType.getName())) {
            //for string
            return objectMapper.writeValueAsString(wrapper);
        }

        return wrapper;
    }
}
