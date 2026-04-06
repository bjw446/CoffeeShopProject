package com.example.coffee_shop_project.domain.menu.controller;

import com.example.coffee_shop_project.common.enums.ErrorStatus;
import com.example.coffee_shop_project.domain.menu.dto.MenuResponse;
import com.example.coffee_shop_project.domain.menu.enums.Category;
import com.example.coffee_shop_project.domain.menu.exception.MenuException;
import com.example.coffee_shop_project.domain.menu.service.MenuService;
import com.example.coffee_shop_project.security.SecurityConfig;
import com.example.coffee_shop_project.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(MenuController.class)
@MockBean(JpaMetamodelMappingContext.class)
@Import(SecurityConfig.class)
public class MenuControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private MenuService menuService;

    @Test
    void 메뉴_전체_조회_성공_테스트() throws Exception {
        // given
        MenuResponse response = MenuResponse.builder()
                .id(1L)
                .name("아메리카노")
                .price(3000L)
                .category(Category.COFFEE)
                .build();

        Page<MenuResponse> page = new PageImpl<>(List.of(response), PageRequest.of(0, 10), 1);
        given(menuService.findAllMenu(any())).willReturn(page);

        // when & then
        mockMvc.perform(get("/menus")
                        .param("page", "1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200_READ_SUCCESS"))
                .andExpect(jsonPath("$.data.content[0].name").value("아메리카노"));
    }

    @Test
    void 메뉴_전체_조회_실패_테스트() throws Exception {
        // given
        given(menuService.findAllMenu(any()))
                .willThrow(new MenuException(ErrorStatus.MENU_NOT_FOUND));

        // when & then
        mockMvc.perform(get("/menus")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void 메뉴_단건_조회_성공_테스트() throws Exception {
        // given
        MenuResponse response = MenuResponse.builder()
                .id(1L)
                .name("카페라떼")
                .price(3000L)
                .category(Category.LATTE)
                .build();

        given(menuService.findOneMenu(1L)).willReturn(response);

        // when & then
        mockMvc.perform(get("/menus/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("카페라떼"));
    }

    @Test
    void 메뉴_단건_조회_실패_테스트() throws Exception {
        // given
        Long invalidId = 999L;
        given(menuService.findOneMenu(invalidId))
                .willThrow(new MenuException(ErrorStatus.MENU_NOT_FOUND));

        // when & then
        mockMvc.perform(get("/menus/{menuId}", invalidId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("404_MENU_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("존재하지 않는 메뉴입니다"));
    }

    @Test
    void 메뉴_카테고리_조회_성공_테스트() throws Exception {
        // given
        MenuResponse response = MenuResponse.builder()
                .id(1L)
                .name("카페라떼")
                .price(3000L)
                .category(Category.LATTE)
                .build();

        Page<MenuResponse> page = new PageImpl<>(List.of(response), PageRequest.of(0, 10), 1);
        given(menuService.findMenuByCategory(eq("LATTE"), any())).willReturn(page);

        // when & then
        mockMvc.perform(get("/menus/category/{category}", Category.LATTE)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].category").value("LATTE"));
    }

    @Test
    void 메뉴_카테고리_조회_실패_테스트() throws Exception {
        given(menuService.findMenuByCategory(eq("COFFEE"), any()))
                .willThrow(new MenuException(ErrorStatus.MENU_NOT_FOUND));

        // when & then
        mockMvc.perform(get("/menus/category/{category}", Category.COFFEE)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void 인기_메뉴_조회_성공_테스트() throws Exception {
        // given
        MenuResponse response = MenuResponse.builder()
                .id(1L)
                .name("카페라떼")
                .price(3000L)
                .category(Category.LATTE)
                .build();

        given(menuService.findPopularMenu()).willReturn(List.of(response));

        // when & then
        mockMvc.perform(get("/menus/popular")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("카페라떼"));
    }

    @Test
    void 인기_메뉴_조회_실패_테스트() throws Exception {
        // given
        MenuResponse response = MenuResponse.builder()
                .id(1L)
                .name("카페라떼")
                .price(3000L)
                .category(Category.LATTE)
                .build();

        given(menuService.findPopularMenu())
                .willThrow(new MenuException(ErrorStatus.MENU_NOT_FOUND));

        // when & then
        mockMvc.perform(get("/menus/popular")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
