package com.example.coffee_shop_project.domain.payment.dto;

import com.example.coffee_shop_project.domain.payment.entity.Payment;
import com.example.coffee_shop_project.domain.payment.enums.PayType;
import com.example.coffee_shop_project.domain.payment.enums.PaymentStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class PaymentResponse {
    private final Long id;
    private final Long userId;
    private final Long orderId;
    private final BigDecimal amount;
    private final PayType payType;
    private final PaymentStatus paymentStatus;

    public static PaymentResponse from(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .userId(payment.getUser().getId())
                .orderId(payment.getOrder().getId())
                .amount(payment.getAmount())
                .payType(payment.getPayType())
                .paymentStatus(payment.getPaymentStatus())
                .build();
    }
}
