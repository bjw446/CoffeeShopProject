package com.example.coffee_shop_project.domain.order.service;

import com.example.coffee_shop_project.common.enums.ErrorStatus;
import com.example.coffee_shop_project.domain.order.dto.CreateOrderRequest;
import com.example.coffee_shop_project.domain.order.dto.OrderResponse;
import com.example.coffee_shop_project.domain.order.entity.Order;
import com.example.coffee_shop_project.domain.order.enums.OrderStatus;
import com.example.coffee_shop_project.domain.order.enums.OrderType;
import com.example.coffee_shop_project.domain.order.repository.OrderRepository;
import com.example.coffee_shop_project.domain.orderitems.dto.CreateOrderItems;
import com.example.coffee_shop_project.domain.orderitems.exception.OrderItemsException;
import com.example.coffee_shop_project.domain.orderitems.repository.OrderItemsRepository;
import com.example.coffee_shop_project.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderItemsRepository orderItemsRepository;

    @Test
    void 주문_생성_성공_테스트() {
        // given
        CreateOrderItems items = new CreateOrderItems();
        ReflectionTestUtils.setField(items, "menuName", "아메리카노");
        ReflectionTestUtils.setField(items, "price", 3000L);
        ReflectionTestUtils.setField(items, "quantity", 2L);

        CreateOrderRequest request = new CreateOrderRequest();
        ReflectionTestUtils.setField(request, "orderType", OrderType.KIOSK);
        ReflectionTestUtils.setField(request, "items", List.of(items));

        given(orderRepository.findMaxOrderNumber()).willReturn(0L);

        Order order = Order.builder()
                .user(null)
                .orderNumber(1L)
                .totalAmount(6000L)
                .orderType(OrderType.KIOSK)
                .orderStatus(OrderStatus.PENDING)
                .build();

        ReflectionTestUtils.setField(order, "id", 1L);

        given(orderRepository.save(any(Order.class))).willReturn(order);

        // when
        OrderResponse response = orderService.createOrder(request);

        // then
        assertEquals(1L, response.getOrderNumber());
        assertEquals(6000, response.getTotalAmount());
        assertEquals(OrderStatus.PENDING, response.getOrderStatus());
    }

    @Test
    void 주문_생성_실패_테스트() {
        // given
        CreateOrderItems items = new CreateOrderItems();
        ReflectionTestUtils.setField(items, "menuName", "아메리카노");
        ReflectionTestUtils.setField(items, "price", 3000L);
        ReflectionTestUtils.setField(items, "quantity", 2L);

        CreateOrderRequest request = new CreateOrderRequest();
        ReflectionTestUtils.setField(request, "orderType", OrderType.KIOSK);
        ReflectionTestUtils.setField(request, "items", List.of(items));

        given(orderRepository.findMaxOrderNumber()).willReturn(0L);

        Order order = Order.builder()
                .user(null)
                .orderNumber(1L)
                .totalAmount(6000L)
                .orderType(OrderType.KIOSK)
                .orderStatus(OrderStatus.PENDING)
                .build();

        ReflectionTestUtils.setField(order, "id", 1L);

        given(orderRepository.save(any(Order.class))).willThrow(new OrderItemsException(ErrorStatus.ORDER_ITEMS_NOT_FOUND));

        // when & then
        assertThrows(OrderItemsException.class, () -> orderService.createOrder(request));
    }
}
