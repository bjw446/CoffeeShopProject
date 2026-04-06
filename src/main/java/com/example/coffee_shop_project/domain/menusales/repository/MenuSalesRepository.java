package com.example.coffee_shop_project.domain.menusales.repository;

import com.example.coffee_shop_project.domain.menu.entity.Menu;
import com.example.coffee_shop_project.domain.menusales.entity.MenuSales;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MenuSalesRepository extends JpaRepository<MenuSales, Long> {
    Optional<MenuSales> findByMenu(Menu menu);
}
