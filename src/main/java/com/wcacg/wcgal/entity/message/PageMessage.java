package com.wcacg.wcgal.entity.message;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Stream;

public record PageMessage<T>(int code, String message, Long count, Integer pages, Stream<T> data) implements IMessage<Stream<T>>{
    public static <T> PageMessage<T> success(Page<T> data) {
        return new PageMessage<>(HttpStatus.OK.value(), "请求成功!", data.getTotalElements(), data.getTotalPages(), data.stream());
    }

    public static <T> PageMessage<T> success(Page<?> pageData, List<T> data) {
        return new PageMessage<>(HttpStatus.OK.value(), "请求成功!", pageData.getTotalElements(), pageData.getTotalPages(), data.stream());
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
    public Stream<T> getData() {
        return data;
    }

    public Long getCount() {
        return count;
    }

    public Integer getPages() {
        return pages;
    }
}
