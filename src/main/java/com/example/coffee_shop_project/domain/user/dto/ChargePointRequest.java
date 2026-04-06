package com.example.coffee_shop_project.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class ChargePointRequest {
    @NotBlank
    @Length(max = 20)
    private String membershipNumber;

    @NotNull
    private Long point;
}
