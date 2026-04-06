package com.example.coffee_shop_project.domain.orderitems.exception;

import com.example.coffee_shop_project.common.enums.ErrorStatus;
import com.example.coffee_shop_project.common.exception.BaseException;

public class OrderItemsException extends BaseException {
    public OrderItemsException(ErrorStatus errorStatusCode) {
        super(errorStatusCode.getHttpStatus(), errorStatusCode);
    }
}
