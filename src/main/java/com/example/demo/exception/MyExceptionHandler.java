package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

//@ControllerAdvice
public class MyExceptionHandler {

//    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handlerGenericError(MethodArgumentNotValidException ex){
        String msg = ex.getBindingResult().getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining(","));
        return new ResponseEntity<>(msg, HttpStatus.UNPROCESSABLE_ENTITY);
    }

}