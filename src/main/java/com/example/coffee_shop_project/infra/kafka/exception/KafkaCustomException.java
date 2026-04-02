package com.example.coffee_shop_project.infra.kafka.exception;

import com.example.coffee_shop_project.common.enums.ErrorStatus;
import com.example.coffee_shop_project.common.exception.BaseException;

public class KafkaCustomException extends BaseException {
    public KafkaCustomException(ErrorStatus errorStatusCode) {
        super(errorStatusCode.getHttpStatus(), errorStatusCode);
    }
}
