package com.example.coffee_shop_project.domain.menu.service;

import com.example.coffee_shop_project.common.enums.ErrorStatus;
import com.example.coffee_shop_project.domain.menu.dto.CreateMenuRequest;
import com.example.coffee_shop_project.domain.menu.dto.MenuResponse;
import com.example.coffee_shop_project.domain.menu.entity.Menu;
import com.example.coffee_shop_project.domain.menu.exception.MenuException;
import com.example.coffee_shop_project.domain.menu.repository.MenuRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuService {
    private final MenuRepository menuRepository;

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

    public Page<MenuResponse> findPopularMenu(Pageable pageable) {
        Page<Menu> menus = menuRepository.findTop10Menus(pageable);

        return menus.map(MenuResponse::from);
    }
}
