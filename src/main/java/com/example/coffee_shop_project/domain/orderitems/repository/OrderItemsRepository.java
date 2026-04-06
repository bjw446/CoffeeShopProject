package com.example.coffee_shop_project.domain.orderitems.repository;

import com.example.coffee_shop_project.domain.orderitems.entity.OrderItems;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderItemsRepository extends JpaRepository<OrderItems, Long> {
    @Query("SELECT oi.menuName, SUM(oi.quantity) as total FROM OrderItems oi " +
            "WHERE oi.createdAt >= :start GROUP BY oi.menuName ORDER BY total DESC")
    List<Object[]> findTopMenus(LocalDateTime start, Pageable pageable);
}
