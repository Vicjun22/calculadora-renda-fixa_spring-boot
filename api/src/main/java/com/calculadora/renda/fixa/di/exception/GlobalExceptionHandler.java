package com.calculadora.renda.fixa.di.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    // BAD_REQUEST          400
    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<Object> handlerExceptionResolver(InvalidParameterException exception) {
        log.error(exception.getMessage(), exception.getCause());

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("erro", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // NOT_FOUND            404
    @ExceptionHandler(NotFoundParameterException.class)
    public ResponseEntity<Object> handlerExceptionResolver(NotFoundParameterException exception) {
        log.error(exception.getMessage(), exception.getCause());

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("erro", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}
