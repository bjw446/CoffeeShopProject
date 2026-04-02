package com.example.coffee_shop_project.domain.menusales.service;

import com.example.coffee_shop_project.common.enums.ErrorStatus;
import com.example.coffee_shop_project.domain.menu.entity.Menu;
import com.example.coffee_shop_project.domain.menu.exception.MenuException;
import com.example.coffee_shop_project.domain.menu.repository.MenuRepository;
import com.example.coffee_shop_project.domain.menusales.entity.MenuSales;
import com.example.coffee_shop_project.domain.menusales.repository.MenuSalesRepository;
import com.example.coffee_shop_project.domain.order.dto.OrderCreatedEvent;
import com.example.coffee_shop_project.domain.orderitems.dto.CreateOrderItems;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuSalesService {
    private final MenuSalesRepository menuSalesRepository;
    private final MenuRepository menuRepository;

    public void updateSales(OrderCreatedEvent event) {

        for (CreateOrderItems item : event.getItems()) {

            Menu menu = menuRepository.findByName(item.getMenuName())
                    .orElseThrow(() -> new MenuException(ErrorStatus.MENU_NOT_FOUND));

            MenuSales menuSales = menuSalesRepository.findByMenu(menu)
                    .orElseGet(() -> MenuSales.builder()
                            .menu(menu)
                            .totalQuantity(0L)
                            .totalAmount(0L)
                            .build()
                    );

            menuSales.increase(item.getQuantity(), item.getPrice() * item.getQuantity());

            menuSalesRepository.save(menuSales);
        }
    }
}
