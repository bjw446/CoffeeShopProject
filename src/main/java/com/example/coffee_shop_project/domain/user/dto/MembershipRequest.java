package com.example.coffee_shop_project.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MembershipRequest {
    @NotBlank
    private String membershipNumber;
}
