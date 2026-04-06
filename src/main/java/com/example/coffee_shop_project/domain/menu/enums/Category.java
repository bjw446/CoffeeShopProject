package com.example.coffee_shop_project.domain.menu.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Category {
    COFFEE("COFFEE"),
    ADE("ADE"),
    SMOOTHIE("SMOOTHIE"),
    TEA("TEA"),
    DESSERT("DESSERT"),
    LATTE("LATTE");

    private final String categoryName;
}
