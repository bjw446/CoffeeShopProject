package com.example.coffee_shop_project.domain.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatus {
    ACTIVE("ACTIVE"),
    DELETED("DELETED");

    private final String status;
}
