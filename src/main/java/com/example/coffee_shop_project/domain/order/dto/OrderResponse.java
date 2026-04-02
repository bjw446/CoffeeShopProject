package com.example.coffee_shop_project.domain.order.dto;

import com.example.coffee_shop_project.domain.order.entity.Order;
import com.example.coffee_shop_project.domain.order.enums.OrderStatus;
import com.example.coffee_shop_project.domain.order.enums.OrderType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderResponse {
    private final Long id;
    private final Long orderNumber;
    private final Long totalAmount;
    private final OrderType orderType;
    private final OrderStatus orderStatus;

    public static OrderResponse from(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .totalAmount(order.getTotalAmount())
                .orderType(order.getOrderType())
                .orderStatus(order.getOrderStatus())
                .build();
    }
}
