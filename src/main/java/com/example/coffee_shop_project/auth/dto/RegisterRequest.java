package com.example.coffee_shop_project.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class RegisterRequest {
    @NotBlank
    @Length(max = 30)
    private String name;

    @NotBlank
    @Length(max = 50)
    private String email;

    @NotBlank
    @Length(max = 255)
    private String password;

    @NotBlank
    @Pattern(regexp = "^010-[\\d*]{4}-\\d{4}$")
    private String phone;

}
