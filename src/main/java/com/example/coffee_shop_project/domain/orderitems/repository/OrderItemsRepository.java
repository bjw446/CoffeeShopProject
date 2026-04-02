package com.example.coffee_shop_project.domain.orderitems.repository;

import com.example.coffee_shop_project.domain.orderitems.entity.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;




public interface OrderItemsRepository extends JpaRepository<OrderItems, Long> {

}
