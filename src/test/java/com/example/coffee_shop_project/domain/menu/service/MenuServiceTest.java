package com.example.coffee_shop_project.domain.menu.service;

import com.example.coffee_shop_project.common.enums.ErrorStatus;
import com.example.coffee_shop_project.domain.menu.dto.MenuResponse;
import com.example.coffee_shop_project.domain.menu.entity.Menu;
import com.example.coffee_shop_project.domain.menu.enums.Category;
import com.example.coffee_shop_project.domain.menu.exception.MenuException;
import com.example.coffee_shop_project.domain.menu.repository.MenuRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private MenuService menuService;

    @Test
    void 메뉴_전체_조회_성공_테스트() {
        // given
        Menu menu = Menu.builder()
                .name("아메리카노")
                .price(3000L)
                .category(Category.COFFEE)
                .build();

        ReflectionTestUtils.setField(menu, "id", 1L);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Menu> page = new PageImpl<>(List.of(menu), pageable, 1);

        given(menuRepository.findAll(pageable)).willReturn(page);

        // when
        Page<MenuResponse> result = menuService.findAllMenu(pageable);

        // then
        assertEquals(1, result.getTotalElements());
        assertEquals("아메리카노", result.getContent().get(0).getName());
    }

    @Test
    void 메뉴_전체_조회_실패_테스트() {
        // given
        given(menuRepository.findAll(any(Pageable.class))).willThrow(new MenuException(ErrorStatus.MENU_NOT_FOUND));

        // when & then
        assertThrows(MenuException.class, () -> menuService.findAllMenu(PageRequest.of(0, 10)));
    }

    @Test
    void 메뉴_단건_조회_성공_테스트() {
        // given
        Menu menu = Menu.builder()
                .name("카페라떼")
                .price(3000L)
                .category(Category.LATTE)
                .build();

        ReflectionTestUtils.setField(menu, "id", 1L);

        given(menuRepository.findById(1L)).willReturn(Optional.of(menu));

        // when
        MenuResponse response = menuService.findOneMenu(1L);

        // then
        assertEquals("카페라떼", response.getName());
    }

    @Test
    void 메뉴_단건_조회_실패_테스트() {
        // given
        Menu menu = Menu.builder()
                .name("아메리카노")
                .price(3000L)
                .category(Category.COFFEE)
                .build();

        ReflectionTestUtils.setField(menu, "id", 1L);

        // when & then
        assertThrows(MenuException.class, () -> menuService.findOneMenu(999L));
    }

    @Test
    void 메뉴_카테고리_조회_성공_테스트() {
        // given
        Menu menu = Menu.builder()
                .name("아메리카노")
                .price(3000L)
                .category(Category.COFFEE)
                .build();

        ReflectionTestUtils.setField(menu, "id", 1L);

        Page<Menu> page = new PageImpl<>(List.of(menu));
        given(menuRepository.findByCategory(eq(Category.COFFEE.getCategoryName()), any())).willReturn(page);

        // when
        Page<MenuResponse> result = menuService.findMenuByCategory(Category.COFFEE.getCategoryName(), PageRequest.of(0, 10));

        // then
        assertEquals(Category.COFFEE, result.getContent().get(0).getCategory());
    }

    @Test
    void 메뉴_카테고리_조회_실패_테스트() {
        // given
        Menu menu = Menu.builder()
                .name("아메리카노")
                .price(3000L)
                .category(Category.COFFEE)
                .build();

        ReflectionTestUtils.setField(menu, "id", 1L);

        given(menuRepository.findByCategory(eq("INVALID"), any()))
                .willThrow(new MenuException(ErrorStatus.MENU_NOT_FOUND));

        // when & then
        assertThrows(RuntimeException.class, () -> menuService.findMenuByCategory("INVALID", PageRequest.of(0, 10)));
    }

    @Test
    void 인기_메뉴_조회_성공_테스트() {
        // given
        Menu menu = Menu.builder()
                .name("아메리카노")
                .price(3000L)
                .category(Category.COFFEE)
                .build();

        ReflectionTestUtils.setField(menu, "id", 1L);

        Page<Menu> page = new PageImpl<>(List.of(menu));
        given(menuRepository.findTop10Menus(any())).willReturn(page);

        // when
        Page<MenuResponse> result = menuService.findPopularMenu(PageRequest.of(0, 10));

        // then
        assertEquals("아메리카노", result.getContent().get(0).getName());
    }

    @Test
    void 인기_메뉴_조회_실패_테스트() {
        // given
        given(menuRepository.findTop10Menus(any()))
                .willThrow(new MenuException(ErrorStatus.MENU_NOT_FOUND));

        // when & then
        assertThrows(MenuException.class, () -> menuService.findPopularMenu(PageRequest.of(0, 10)));
    }
}
