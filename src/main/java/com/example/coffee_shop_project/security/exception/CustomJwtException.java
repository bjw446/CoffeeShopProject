package com.example.coffee_shop_project.security.exception;

import com.example.coffee_shop_project.common.enums.ErrorStatus;
import com.example.coffee_shop_project.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class CustomJwtException extends BaseException {

    public CustomJwtException(ErrorStatus errorStatusCode) {
        super(errorStatusCode.getHttpStatus(), errorStatusCode);
    }
}
