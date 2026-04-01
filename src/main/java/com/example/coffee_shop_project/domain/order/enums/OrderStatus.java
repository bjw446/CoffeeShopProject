package com.example.coffee_shop_project.domain.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    PENDING("PENDING"),
    PAID("PAID"),
    CANCELLED("CANCELLED");

    private final String status;
}
