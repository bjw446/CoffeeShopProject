package com.example.coffee_shop_project.domain.menu.service;

import com.example.coffee_shop_project.common.enums.ErrorStatus;
import com.example.coffee_shop_project.domain.menu.dto.CreateMenuRequest;
import com.example.coffee_shop_project.domain.menu.dto.MenuResponse;
import com.example.coffee_shop_project.domain.menu.entity.Menu;
import com.example.coffee_shop_project.domain.menu.enums.Category;
import com.example.coffee_shop_project.domain.menu.exception.MenuException;
import com.example.coffee_shop_project.domain.menu.repository.MenuRepository;
import com.example.coffee_shop_project.domain.orderitems.repository.OrderItemsRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuService {
    private final MenuRepository menuRepository;
    private final OrderItemsRepository orderItemsRepository;

    public MenuResponse createMenu(CreateMenuRequest request) {
        Menu menu = Menu.builder()
                .name(request.getName())
                .price(request.getPrice())
                .category(request.getCategory())
                .build();

        Menu savedMenu = menuRepository.save(menu);

        return MenuResponse.from(savedMenu);
    }

    @Transactional(readOnly = true)
    public Page<MenuResponse> findAllMenu(Pageable pageable) {
        Page<Menu> menus = menuRepository.findAll(pageable);

        return menus.map(MenuResponse::from);
    }

    @Transactional(readOnly = true)
    public MenuResponse findOneMenu(Long menuId) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(
                () -> new MenuException(ErrorStatus.MENU_NOT_FOUND)
        );

        return MenuResponse.from(menu);
    }

    @Transactional(readOnly = true)
    public Page<MenuResponse> findMenuByCategory(String category, Pageable pageable) {
        Page<Menu> menus = menuRepository.findByCategory(category, pageable);

        return menus.map(MenuResponse::from);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> findPopularMenu() {
        LocalDateTime start = LocalDateTime.now().minusDays(7);

        List<Object[]> results = orderItemsRepository.findTopMenus(start, PageRequest.of(0, 3));

        return results.stream()
                .map(r -> MenuResponse.builder()
                        .id((Long) r[0])
                        .name((String) r[1])
                        .price((Long) r[2])
                        .category((Category) r[3])
                        .build())
                .toList();
    }
}
