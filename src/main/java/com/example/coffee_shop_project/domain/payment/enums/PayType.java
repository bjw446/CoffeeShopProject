package com.example.coffee_shop_project.domain.payment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PayType {
    POINT("POINT"),
    CARD("CARD"),
    CASH("CASH");

    private final String payTypeName;
}