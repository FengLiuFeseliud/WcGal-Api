package com.wcacg.wcgal.advice;

import com.wcacg.wcgal.entity.message.ResponseMessage;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class HttpStatusCodeAdvice implements ResponseBodyAdvice<ResponseMessage<?>> {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return returnType.getParameterType().isAssignableFrom(ResponseMessage.class);
    }

    @Override
    public ResponseMessage<?> beforeBodyWrite(ResponseMessage<?> body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body != null) {
            response.setStatusCode(HttpStatusCode.valueOf(body.code()));
        }
        return body;
    }
}
