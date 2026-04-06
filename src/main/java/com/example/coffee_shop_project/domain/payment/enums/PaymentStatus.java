package com.example.coffee_shop_project.domain.payment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentStatus {
    SUCCESS("SUCCESS"),
    FAIL("FAIL"),
    CANCELLED("CANCELLED");

    private final String payTypeName;
}