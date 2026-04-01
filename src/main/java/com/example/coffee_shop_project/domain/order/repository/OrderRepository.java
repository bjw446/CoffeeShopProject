package com.example.coffee_shop_project.domain.order.repository;

import com.example.coffee_shop_project.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
