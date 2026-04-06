package com.example.coffee_shop_project.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentSuccessEvent {
    private Long orderId;
    private Long userId;
    private Long totalAmount;
}
