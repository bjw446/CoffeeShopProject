package com.example.coffee_shop_project.domain.order.controller;

import com.example.coffee_shop_project.common.enums.ErrorStatus;
import com.example.coffee_shop_project.common.enums.SuccessStatus;
import com.example.coffee_shop_project.domain.order.dto.CreateOrderRequest;
import com.example.coffee_shop_project.domain.order.dto.OrderResponse;
import com.example.coffee_shop_project.domain.order.enums.OrderStatus;
import com.example.coffee_shop_project.domain.order.enums.OrderType;
import com.example.coffee_shop_project.domain.order.exception.OrderException;
import com.example.coffee_shop_project.domain.order.service.OrderService;
import com.example.coffee_shop_project.domain.orderitems.exception.OrderItemsException;
import com.example.coffee_shop_project.security.SecurityConfig;
import com.example.coffee_shop_project.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@MockBean(JpaMetamodelMappingContext.class)
@Import(SecurityConfig.class)
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private OrderService orderService;

    @Test
    void 주문_생성_성공_테스트() throws Exception {
        // given
        String requestBody = """
                {
                    "userId": 1,
                    "orderType": "KIOSK",
                    "items": [
                        {
                            "menuName": "아메리카노",
                            "price": 3000,
                            "quantity": 2
                        }
                    ]
                }
                """;

        OrderResponse response = OrderResponse.builder()
                .id(1L)
                .orderNumber(1L)
                .totalAmount(6000L)
                .orderType(OrderType.KIOSK)
                .orderStatus(OrderStatus.PENDING)
                .build();

        given(orderService.createOrder(any(CreateOrderRequest.class))).willReturn(response);

        // when & then
        mockMvc.perform(post("/orders")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("201_CREATE_SUCCESS"))
                .andExpect(jsonPath("$.data.orderNumber").value(1L))
                .andExpect(jsonPath("$.data.totalAmount").value(6000))
                .andExpect(jsonPath("$.data.orderStatus").value("PENDING"));
    }

    @Test
    void 주문_생성_실패_테스트() throws Exception {
        // given
        String requestBody = """
                {
                    "userId": 1,
                    "orderType": "KIOSK",
                    "items": [
                        {
                            "menuName": "카페라떼",
                            "price": 4000,
                            "quantity": 2
                        }
                    ]
                }
                """;

        given(orderService.createOrder(any(CreateOrderRequest.class)))
                .willThrow(new OrderItemsException(ErrorStatus.ORDER_ITEMS_NOT_FOUND));

        // when & then
        mockMvc.perform(post("/orders")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("404_ORDER_ITEMS_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("주문 상품이 존재하지 않습니다"));
    }

    @Test
    void 주문_조회_성공_테스트() throws Exception {
        // given
        OrderResponse response = OrderResponse.builder()
                .id(1L)
                .orderNumber(1L)
                .totalAmount(6000L)
                .orderType(OrderType.KIOSK)
                .orderStatus(OrderStatus.PENDING)
                .build();

        given(orderService.findOneOrder(1L)).willReturn(response);

        // when & then
        mockMvc.perform(get("/orders/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SuccessStatus.READ_SUCCESS.getSuccessCode()))
                .andExpect(jsonPath("$.data.orderNumber").value(1L))
                .andExpect(jsonPath("$.data.orderType").value("KIOSK"))
                .andExpect(jsonPath("$.data.orderStatus").value("PENDING"));
    }

    @Test
    void 주문_조회_실패_테스트() throws Exception {
        // given
        Long invalidId = 99L;

        given(orderService.findOneOrder(invalidId)).willThrow(new OrderException(ErrorStatus.ORDER_NOT_FOUND));

        // when & then
        mockMvc.perform(get("/orders/{orderId}", invalidId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(ErrorStatus.ORDER_NOT_FOUND.getErrorCode()))
                .andExpect(jsonPath("$.message").value(ErrorStatus.ORDER_NOT_FOUND.getMessage()));
    }
}
