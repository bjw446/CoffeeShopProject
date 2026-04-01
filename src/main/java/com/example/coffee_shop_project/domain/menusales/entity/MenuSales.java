package com.example.coffee_shop_project.domain.menusales.entity;

import com.example.coffee_shop_project.domain.menu.entity.Menu;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "menu_sales")
public class MenuSales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @NotNull
    private Long totalQuantity;

    @NotNull
    private Long totalAmount;
}
