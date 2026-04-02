package com.example.coffee_shop_project.domain.menu.repository;

import com.example.coffee_shop_project.domain.menu.entity.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    Page<Menu> findByCategory(String category, Pageable pageable);

    @Query("SELECT m FROM MenuSales ms JOIN ms.menu m ORDER BY ms.totalQuantity DESC")
    Page<Menu> findTop10Menus(Pageable pageable);

    Optional<Menu> findByName(String menuName);
}
