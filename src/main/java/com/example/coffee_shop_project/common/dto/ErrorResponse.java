package com.example.coffee_shop_project.common.dto;

import com.example.coffee_shop_project.common.enums.ErrorStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final HttpStatus status;
    private final String code;
    private final String message;
    private final String path;

    public static ErrorResponse of(ErrorStatus errorStatus, String path) {
        return new ErrorResponse(
                errorStatus.getHttpStatus(),
                errorStatus.getErrorCode(),
                errorStatus.getMessage(),
                path
        );
    }
}
