package com.example.coffee_shop_project.domain.orderitems.entity;

import com.example.coffee_shop_project.common.entity.ModifiableEntity;
import com.example.coffee_shop_project.domain.order.entity.Order;
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
@Table(name = "order_items")
public class OrderItems extends ModifiableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @NotBlank
    @Length(max = 20)
    private String menuName;

    @NotNull
    private Long price;

    @NotNull
    private Long quantity;

    @Builder
    public OrderItems(Order order, String menuName, Long price, Long quantity) {
        this.order = order;
        this.menuName = menuName;
        this.price = price;
        this.quantity = quantity;
    }

    public void connectOrder(Order order) {
        this.order = order;
    }
}
