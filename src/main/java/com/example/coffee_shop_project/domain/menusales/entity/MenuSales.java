package com.example.coffee_shop_project.domain.menusales.entity;

import com.example.coffee_shop_project.domain.menu.entity.Menu;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
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
    @JoinColumn(name = "menu_id", unique = true)
    private Menu menu;

    @NotNull
    private Long totalQuantity;

    @NotNull
    private Long totalAmount;

    @Builder
    public MenuSales(Menu menu, Long totalQuantity, Long totalAmount) {
        this.menu = menu;
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
    }

    public void increase(Long quantity, long amount) {
        this.totalQuantity += quantity;
        this.totalAmount += amount;
    }
}
