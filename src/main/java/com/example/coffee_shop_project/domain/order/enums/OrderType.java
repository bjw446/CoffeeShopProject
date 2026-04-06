package com.example.coffee_shop_project.domain.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderType {
    KIOSK("KIOSK"),
    COUNTER("COUNTER");

    private final String orderType;
}
