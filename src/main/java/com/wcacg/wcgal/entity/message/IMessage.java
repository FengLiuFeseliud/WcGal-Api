package com.wcacg.wcgal.entity.message;

public interface IMessage<T> {
    Integer getCode();
    String getMessage();
    T getData();
}
