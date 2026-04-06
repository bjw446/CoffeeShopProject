package com.example.coffee_shop_project.infra.outbox.exception;

import com.example.coffee_shop_project.common.enums.ErrorStatus;
import com.example.coffee_shop_project.common.exception.BaseException;

public class EventCustomException extends BaseException {
    public EventCustomException(ErrorStatus errorStatusCode) {
        super(errorStatusCode.getHttpStatus(), errorStatusCode);
    }
}
