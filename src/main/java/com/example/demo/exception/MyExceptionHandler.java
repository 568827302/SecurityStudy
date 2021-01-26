package com.example.demo.exception;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    // @ResponseBody
    public ResponseEntity<Object> handlerGenericError(MethodArgumentNotValidException ex){
        String msg = ex.getBindingResult().getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining(","));
        return new ResponseEntity<>(msg, HttpStatus.UNPROCESSABLE_ENTITY);
    }

}