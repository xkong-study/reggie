package com.example.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;
@Slf4j
public class CustomException extends RuntimeException{
    public CustomException(String message){
           super(message);
    }

    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException exception){
        log.info(String.valueOf(exception));
        return R.error(exception.getMessage());
    }
}
