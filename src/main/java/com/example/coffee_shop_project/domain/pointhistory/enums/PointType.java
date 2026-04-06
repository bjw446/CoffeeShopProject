package com.example.coffee_shop_project.domain.pointhistory.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PointType {
    CHARGE("CHARGE"),
    USE("USE"),
    REFUND("REFUND");

    private final String pointType;
}
