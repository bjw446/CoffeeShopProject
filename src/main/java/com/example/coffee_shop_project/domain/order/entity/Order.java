package com.example.coffee_shop_project.domain.order.entity;

import com.example.coffee_shop_project.common.entity.DeletableEntity;
import com.example.coffee_shop_project.common.enums.ErrorStatus;
import com.example.coffee_shop_project.domain.order.enums.OrderStatus;
import com.example.coffee_shop_project.domain.order.enums.OrderType;
import com.example.coffee_shop_project.domain.order.exception.OrderException;
import com.example.coffee_shop_project.domain.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order extends DeletableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @Column(unique = true)
    private Long orderNumber;

    @NotNull
    private Long totalAmount;

    @NotNull
    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    @NotNull
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Builder
    public Order(User user, Long orderNumber, Long totalAmount, OrderType orderType, OrderStatus orderStatus) {
        this.user = user;
        this.orderNumber = orderNumber;
        this.totalAmount = totalAmount;
        this.orderType = orderType;
        this.orderStatus = orderStatus;
    }

    public void cancelOrder() {
        switch (this.orderStatus) {
            case CANCELLED -> throw new OrderException(ErrorStatus.ALREADY_CANCELLED_ORDER);
            case PAID -> throw new OrderException(ErrorStatus.ALREADY_PAID_ORDER);
        }

        this.orderStatus = OrderStatus.CANCELLED;
    }

    public void cancelOrderByAdmin() {
        if (this.orderStatus == OrderStatus.CANCELLED) {
            throw new OrderException(ErrorStatus.ALREADY_CANCELLED_ORDER);
        }

        this.orderStatus = OrderStatus.CANCELLED;
    }

    public void paid() {
        switch (this.orderStatus) {
            case CANCELLED -> throw new OrderException(ErrorStatus.ALREADY_CANCELLED_ORDER);
            case PAID -> throw new OrderException(ErrorStatus.ALREADY_PAID_ORDER);
        }

        this.orderStatus = OrderStatus.PAID;

    }
}
