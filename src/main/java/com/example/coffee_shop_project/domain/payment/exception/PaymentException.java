package com.example.coffee_shop_project.domain.payment.exception;

import com.example.coffee_shop_project.common.enums.ErrorStatus;
import com.example.coffee_shop_project.common.exception.BaseException;

public class PaymentException extends BaseException {
    public PaymentException(ErrorStatus errorStatusCode) {
        super(errorStatusCode.getHttpStatus(), errorStatusCode);
    }
}
