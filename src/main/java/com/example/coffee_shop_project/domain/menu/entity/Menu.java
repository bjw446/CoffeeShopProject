package com.example.coffee_shop_project.domain.menu.entity;

import com.example.coffee_shop_project.common.entity.DeletableEntity;
import com.example.coffee_shop_project.domain.menu.enums.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "menus")
public class Menu extends DeletableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Length(max = 20)
    private String name;

    @NotNull
    private Long price;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Category category;

    @Builder
    public Menu (String name, Long price, Category category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }
}
