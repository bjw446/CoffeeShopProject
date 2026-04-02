package com.example.coffee_shop_project.domain.order.dto;


import com.example.coffee_shop_project.domain.order.enums.OrderType;
import com.example.coffee_shop_project.domain.orderitems.dto.CreateOrderItems;
import lombok.Getter;


import java.util.List;

@Getter
public class CreateOrderRequest {
    private Long userId;
    private OrderType orderType;
    private List<CreateOrderItems> items;
}
