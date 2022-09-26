package com.example.Exception.controller;

import com.example.Exception.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class DemoExceptionHandler {

    // 捕捉 Exception
    // 因為是所有例外的父類，可以作為例外處理的最後一道防線
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handler(Exception e) {

        ErrorResponse error = new ErrorResponse(e);

        return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
    }

    // 捕捉 MethodArgumentNotValidException (取得 RequestBody 驗證未通過)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handler(MethodArgumentNotValidException e) {

        ErrorResponse error = new ErrorResponse(e);

        return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
    }

    // 捕捉 ConstraintViolationException (取得 url 上的參數檢驗未通過)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handler(ConstraintViolationException e) {

        ErrorResponse error = new ErrorResponse(e);

        return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
    }

}
