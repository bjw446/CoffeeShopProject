package com.example.coffee_shop_project.domain.menu.dto;

import com.example.coffee_shop_project.domain.menu.entity.Menu;
import com.example.coffee_shop_project.domain.menu.enums.Category;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MenuResponse {
    private final Long id;
    private final String name;
    private final Long price;
    private final Category category;

    public static MenuResponse from(Menu menu) {
        return MenuResponse.builder()
                .id(menu.getId())
                .name(menu.getName())
                .price(menu.getPrice())
                .category(menu.getCategory())
                .build();
    }
}
