package com.example.coffee_shop_project.domain.menu.dto;

import com.example.coffee_shop_project.domain.menu.enums.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class CreateMenuRequest {
    @NotBlank
    @Length(max = 20)
    private String name;

    @NotNull
    private Long price;

    @NotNull
    private Category category;
}
