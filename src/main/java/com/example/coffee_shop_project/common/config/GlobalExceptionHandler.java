package com.example.coffee_shop_project.common.config;

import com.example.coffee_shop_project.common.dto.ErrorResponse;
import com.example.coffee_shop_project.common.exception.BaseException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> BaseExceptionHandler(
            BaseException e, HttpServletRequest request
    ) {
        return ResponseEntity.status(e.getErrorStatusCode().getHttpStatus()).body(ErrorResponse.of(e.getErrorStatusCode(), request.getRequestURI()));
    }
}
