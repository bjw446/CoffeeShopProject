package com.example.coffee_shop_project.domain.order.repository;

import com.example.coffee_shop_project.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT COALESCE(MAX(o.orderNumber), 0) FROM Order o")
    Long findMaxOrderNumber();
}
