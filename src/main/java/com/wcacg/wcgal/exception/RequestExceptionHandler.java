package com.wcacg.wcgal.exception;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.wcacg.wcgal.entity.message.ResponseMessage;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
@RestControllerAdvice
public class RequestExceptionHandler {

    /**
     * 接口 404 错误
     * @return 错误响应信息
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseMessage<?> noHandlerFoundException() {
        return ResponseMessage.notFound("不存在的接口...");
    }

    /**
     * 接口请求方式错误
     * @return 错误响应信息
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseMessage<?> httpRequestMethodNotSupportedException() {
        return ResponseMessage.notFound("错误的请求方法...");
    }

    /**
     * 接口请求参数错误
     * @return 错误响应信息
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseMessage<?> MethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        FieldError fieldError = exception.getFieldError();
        if (fieldError == null) {
            return ResponseMessage.error();
        }
        return new ResponseMessage<>(HttpStatus.BAD_REQUEST.value(),
                fieldError.getField() + " 不能为 " + fieldError.getRejectedValue() + " 啦~ QwQ", fieldError.getRejectedValue());
    }

    /**
     * 接口请求未携带参数
     * @return 错误响应信息
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseMessage<?> httpMessageNotReadableException() {
        return new ResponseMessage<>(HttpStatus.BAD_REQUEST.value(), "呜~ 请求体不能为空啦", null);
    }

    /**
     * 数据库保存请求参数错误
     * @return 错误响应信息
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseMessage<?> sqlIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException exception) {
        return new ResponseMessage<>(HttpStatus.BAD_REQUEST.value(), "呜~ 不能为 " + exception.getMessage().split("'")[1] +" 啦", null);
    }

    /**
     * 这个是啥错我忘了 (
     * @return 错误响应信息
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseMessage<?> entityNotFoundException(EntityNotFoundException exception) {
        return new ResponseMessage<>(HttpStatus.NOT_FOUND.value(), exception.getMessage(), null);
    }

    @ExceptionHandler(JWTVerificationException.class)
    public ResponseMessage<?> entityNotFoundException(JWTVerificationException exception) {
        return new ResponseMessage<>(HttpStatus.FORBIDDEN.value(), "你还没未登录呢 ...", null);
    }

    @ExceptionHandler(ClientError.NotTokenException.class)
    public ResponseMessage<?> entityNotFoundException(ClientError.NotTokenException exception) {
        return new ResponseMessage<>(HttpStatus.FORBIDDEN.value(), exception.getMessage(), null);
    }

    @ExceptionHandler(ClientError.NotFindException.class)
    public ResponseMessage<?> entityNotFoundException(ClientError.NotFindException exception) {
        return new ResponseMessage<>(HttpStatus.NOT_FOUND.value(), exception.getMessage(), null);
    }

    @ExceptionHandler(ClientError.MaxCreateException.class)
    public ResponseMessage<?> entityNotFoundException(ClientError.MaxCreateException exception) {
        return new ResponseMessage<>(HttpStatus.BAD_REQUEST.value(), exception.getMessage(), null);
    }

    @ExceptionHandler(ClientError.NotPermissionsException.class)
    public ResponseMessage<?> entityNotFoundException(ClientError.NotPermissionsException exception) {
        return new ResponseMessage<>(HttpStatus.FORBIDDEN.value(), exception.getMessage(), null);
    }

    @ExceptionHandler(ClientError.HaveExistedException.class)
    public ResponseMessage<?> entityNotFoundException(ClientError.HaveExistedException exception) {
        return new ResponseMessage<>(HttpStatus.BAD_REQUEST.value(), exception.getMessage(), null);
    }
}
