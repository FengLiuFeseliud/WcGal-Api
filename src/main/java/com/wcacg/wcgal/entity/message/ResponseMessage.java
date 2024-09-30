package com.wcacg.wcgal.entity.message;

import jakarta.validation.constraints.Null;
import org.springframework.http.HttpStatus;

public record ResponseMessage<T>(int code, String message, T data) implements IMessage<T> {
    public static <T> ResponseMessage<T> success(T data) {
        return new ResponseMessage<>(HttpStatus.OK.value(), "请求成功!", data);
    }

    public static <T> ResponseMessage<T> dataError(String message, T data) {
        return new ResponseMessage<>(HttpStatus.BAD_REQUEST.value(), message, data);
    }

    public static ResponseMessage<Null> error() {
        return new ResponseMessage<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "请求错误...", null);
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public T getData() {
        return data;
    }
}
