package com.example.coffee_shop_project.domain.order.controller;

import com.example.coffee_shop_project.common.enums.ErrorStatus;
import com.example.coffee_shop_project.domain.order.dto.OrderResponse;
import com.example.coffee_shop_project.domain.order.enums.OrderStatus;
import com.example.coffee_shop_project.domain.order.enums.OrderType;
import com.example.coffee_shop_project.domain.order.exception.OrderException;
import com.example.coffee_shop_project.domain.order.service.OrderService;
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

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminOrderController.class)
@MockBean(JpaMetamodelMappingContext.class)
@Import(SecurityConfig.class)
public class AdminOrderControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private OrderService orderService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void 관리자_주문_취소_성공_테스트() throws Exception {
        // given
        OrderResponse response = OrderResponse.builder()
                .id(1L)
                .orderNumber(1L)
                .totalAmount(6000L)
                .orderType(OrderType.KIOSK)
                .orderStatus(OrderStatus.PENDING)
                .build();

        doNothing().when(orderService).cancelOrderByAdmin(response.getId());

        // when & then
        mockMvc.perform(post("/admin/orders/1/cancel")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void 관리자_주문_취소_실패_테스트() throws Exception {
        // given
        Long invalidId = 9999L;

        doThrow(new OrderException(ErrorStatus.ORDER_NOT_FOUND)).when(orderService).cancelOrderByAdmin(invalidId);

        // when & then
        mockMvc.perform(post("/admin/orders/{orderId}/cancel", invalidId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
