package com.example.coffee_shop_project.domain.payment.dto;

import com.example.coffee_shop_project.domain.payment.enums.PayType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;
@Getter
public class CreatePaymentRequest {
    private Long userId;

    @NotNull
    private Long orderId;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private PayType payType;
}
