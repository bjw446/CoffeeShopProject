package com.example.coffee_shop_project.domain.orderitems.dto;

import lombok.Getter;

@Getter
public class CreateOrderItems {
    private String menuName;
    private Long price;
    private Long quantity;
}
