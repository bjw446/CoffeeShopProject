package com.example.coffee_shop_project.domain.payment.entity;

import com.example.coffee_shop_project.common.entity.CreatableEntity;
import com.example.coffee_shop_project.common.enums.ErrorStatus;
import com.example.coffee_shop_project.domain.order.entity.Order;
import com.example.coffee_shop_project.domain.payment.enums.PayType;
import com.example.coffee_shop_project.domain.payment.enums.PaymentStatus;
import com.example.coffee_shop_project.domain.payment.exception.PaymentException;
import com.example.coffee_shop_project.domain.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "payments")
public class Payment extends CreatableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @NotNull
    @DecimalMin(value = "0.0")
    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PayType payType;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    public void cancel() {
        switch (paymentStatus) {
            case CANCELLED -> throw new PaymentException(ErrorStatus.ALREADY_CANCELLED_PAYMENT);
            case FAIL -> throw new PaymentException(ErrorStatus.FAILED_PAYMENT);
        }

        this.paymentStatus = PaymentStatus.CANCELLED;
    }
}
