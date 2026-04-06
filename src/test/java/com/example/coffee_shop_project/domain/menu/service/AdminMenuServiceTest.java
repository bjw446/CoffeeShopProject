package com.example.coffee_shop_project.domain.menu.service;

import com.example.coffee_shop_project.common.enums.ErrorStatus;
import com.example.coffee_shop_project.domain.menu.dto.CreateMenuRequest;
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
import org.springframework.test.util.ReflectionTestUtils;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class AdminMenuServiceTest {
    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private MenuService menuService;

    @Test
    void 메뉴_등록_성공_테스트() {
        // given
        CreateMenuRequest request = new CreateMenuRequest();
        ReflectionTestUtils.setField(request, "name", "아메리카노");
        ReflectionTestUtils.setField(request, "price", 3000L);
        ReflectionTestUtils.setField(request, "category", Category.COFFEE);

        Menu menu = Menu.builder()
                .name("아메리카노")
                .price(3000L)
                .category(Category.COFFEE)
                .build();

        ReflectionTestUtils.setField(menu, "id", 1L);

        given(menuRepository.save(any(Menu.class))).willReturn(menu);

        // when
        MenuResponse response = menuService.createMenu(request);

        // then
        assertEquals("아메리카노", response.getName());
        assertEquals(3000, response.getPrice());
        assertEquals(Category.COFFEE, response.getCategory());
    }

    @Test
    void 메뉴_등록_실패_테스트() {
        // given
        CreateMenuRequest request = new CreateMenuRequest();
        ReflectionTestUtils.setField(request, "name", "아메리카노");
        ReflectionTestUtils.setField(request, "price", 3000L);
        ReflectionTestUtils.setField(request, "category", Category.COFFEE);

        Menu menu = Menu.builder()
                .name("아메리카노")
                .price(3000L)
                .category(Category.COFFEE)
                .build();

        ReflectionTestUtils.setField(menu, "id", 1L);

        given(menuRepository.save(any(Menu.class))).willThrow(new MenuException(ErrorStatus.ACCESS_FORBIDDEN));

        // when & then
        assertThrows(MenuException.class, () -> menuService.createMenu(request));
    }
}
