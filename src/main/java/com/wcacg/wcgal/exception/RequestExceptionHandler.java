package com.wcacg.wcgal.exception;

import com.wcacg.wcgal.entity.message.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
@RestControllerAdvice
public class RequestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseMessage<?> MethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        FieldError fieldError = exception.getFieldError();
        if (fieldError == null) {
            return ResponseMessage.error();
        }
        return new ResponseMessage<>(HttpStatus.BAD_REQUEST.value(),
                fieldError.getField() + " 不能为 " + fieldError.getRejectedValue() + " 啦~ QwQ", fieldError.getRejectedValue());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseMessage<?> httpMessageNotReadableException(HttpMessageNotReadableException exception) {
        return new ResponseMessage<>(HttpStatus.BAD_REQUEST.value(), "呜~ 请求体不能为空啦", null);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseMessage<?> sqlIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException exception) {
        return new ResponseMessage<>(HttpStatus.BAD_REQUEST.value(), "呜~ 不能为 " + exception.getMessage().split("'")[1] +" 啦", null);
    }
}
