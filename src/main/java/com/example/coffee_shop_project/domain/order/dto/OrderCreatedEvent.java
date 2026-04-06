package com.example.coffee_shop_project.domain.order.dto;

import com.example.coffee_shop_project.domain.orderitems.dto.CreateOrderItems;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class OrderCreatedEvent {
    private Long orderId;
    private List<CreateOrderItems> items;
}
