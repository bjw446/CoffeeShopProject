package com.example.coffee_shop_project.domain.menu.exception;

import com.example.coffee_shop_project.common.enums.ErrorStatus;
import com.example.coffee_shop_project.common.exception.BaseException;

public class MenuException extends BaseException {

    public MenuException(ErrorStatus errorStatusCode) {
        super(errorStatusCode.getHttpStatus(), errorStatusCode);
    }
}
