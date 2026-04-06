package com.example.coffee_shop_project.auth.dto;

import com.example.coffee_shop_project.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponse {
    private final Long id;
    private final String name;
    private final String email;
    private final String phone;
    private final String membershipNumber;
    private final Long point;
    private final String role;
    private final String status;

    public static AuthResponse from(User user) {
        return AuthResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .membershipNumber(user.getMembershipNumber())
                .point(user.getPoint())
                .role(user.getUserRole().getRoleName())
                .status(user.getUserStatus().getStatus())
                .build();
    }
}
