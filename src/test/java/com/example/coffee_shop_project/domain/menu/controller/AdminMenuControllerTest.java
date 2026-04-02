package com.example.coffee_shop_project.domain.menu.controller;

import com.example.coffee_shop_project.domain.menu.dto.CreateMenuRequest;
import com.example.coffee_shop_project.domain.menu.dto.MenuResponse;
import com.example.coffee_shop_project.domain.menu.enums.Category;
import com.example.coffee_shop_project.domain.menu.service.MenuService;
import com.example.coffee_shop_project.security.SecurityConfig;
import com.example.coffee_shop_project.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(AdminMenuController.class)
@MockBean(JpaMetamodelMappingContext.class)
@Import(SecurityConfig.class)
public class AdminMenuControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private MenuService menuService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void 메뉴_등록_성공_테스트() throws Exception {
        // given
        String requestBody = """
                {
                    "name": "아메리카노",
                    "price": 3000,
                    "category": "COFFEE"
                }
                """;

        MenuResponse response = MenuResponse.builder()
                .id(1L).name("아메리카노").price(3000L).category(Category.COFFEE).build();

        given(menuService.createMenu(any(CreateMenuRequest.class))).willReturn(response);

        // when & then
        mockMvc.perform(post("/admin/menus")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("201_CREATE_SUCCESS"))
                .andExpect(jsonPath("$.data.name").value("아메리카노"))
                .andExpect(jsonPath("$.data.price").value(3000))
                .andExpect(jsonPath("$.data.category").value("COFFEE"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void 메뉴_등록_실패_테스트() throws Exception {
        mockMvc.perform(post("/admin/menus").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());

    }
}
