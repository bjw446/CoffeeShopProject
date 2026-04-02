package com.example.coffee_shop_project.domain.order.service;

import com.example.coffee_shop_project.common.enums.ErrorStatus;
import com.example.coffee_shop_project.domain.order.dto.CreateOrderRequest;
import com.example.coffee_shop_project.domain.order.dto.OrderResponse;
import com.example.coffee_shop_project.domain.order.entity.Order;
import com.example.coffee_shop_project.domain.order.enums.OrderStatus;
import com.example.coffee_shop_project.domain.order.enums.OrderType;
import com.example.coffee_shop_project.domain.order.exception.OrderException;
import com.example.coffee_shop_project.domain.order.repository.OrderRepository;
import com.example.coffee_shop_project.domain.orderitems.dto.CreateOrderItems;
import com.example.coffee_shop_project.domain.orderitems.exception.OrderItemsException;
import com.example.coffee_shop_project.domain.orderitems.repository.OrderItemsRepository;
import com.example.coffee_shop_project.domain.payment.service.PaymentService;
import com.example.coffee_shop_project.domain.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

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

    @Mock
    private PaymentService paymentService;

    @Test
    void 주문_생성_성공_테스트() throws JsonProcessingException {
        // given
        CreateOrderItems items = new CreateOrderItems("아메리카노", 3000L, 2L);

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
        CreateOrderItems items = new CreateOrderItems("아메리카노", 3000L, 2L);

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

    @Test
    void 주문_조회_성공_테스트() {
        // given
        Order order = Order.builder()
                .user(null)
                .orderNumber(1L)
                .totalAmount(8000L)
                .orderType(OrderType.KIOSK)
                .orderStatus(OrderStatus.PENDING)
                .build();

        ReflectionTestUtils.setField(order, "id", 1L);

        given(orderRepository.findById(1L)).willReturn(Optional.of(order));

        // when
        OrderResponse response = orderService.findOneOrder(1L);

        // then
        assertEquals(1L, response.getOrderNumber());
        assertEquals(8000L, response.getTotalAmount());
        assertEquals(OrderType.KIOSK, response.getOrderType());
        assertEquals(OrderStatus.PENDING, response.getOrderStatus());
    }

    @Test
    void 주문_조회_실패_테스트() {
        // given
        Order order = Order.builder()
                .user(null)
                .orderNumber(1L)
                .totalAmount(8000L)
                .orderType(OrderType.KIOSK)
                .orderStatus(OrderStatus.PENDING)
                .build();

        ReflectionTestUtils.setField(order, "id", 1L);

        // when & then
        assertThrows(OrderException.class, () -> orderService.findOneOrder(5L));
    }

    @Test
    void 주문_취소_성공_테스트() {
        // given
        Order order = Order.builder()
                .user(null)
                .orderNumber(1L)
                .totalAmount(8000L)
                .orderType(OrderType.KIOSK)
                .orderStatus(OrderStatus.PENDING)
                .build();

        ReflectionTestUtils.setField(order, "id", 1L);

        given(orderRepository.findById(1L)).willReturn(Optional.of(order));

        // when
        orderService.cancelOrder(1L);

        // then
        assertEquals(OrderStatus.CANCELLED, order.getOrderStatus());
    }

    @Test
    void 주문_취소_실패_테스트() {
        // given
        Order order = Order.builder()
                .user(null)
                .orderNumber(1L)
                .totalAmount(8000L)
                .orderType(OrderType.KIOSK)
                .orderStatus(OrderStatus.CANCELLED)
                .build();

        ReflectionTestUtils.setField(order, "id", 1L);

        given(orderRepository.findById(1L)).willReturn(Optional.of(order));

        // when & then
        assertThrows(OrderException.class, () -> orderService.cancelOrder(1L));
    }

    @Test
    void 관리자_주문_취소_성공_테스트() {
        // given
        Order order = Order.builder()
                .user(null)
                .orderNumber(1L)
                .totalAmount(8000L)
                .orderType(OrderType.KIOSK)
                .orderStatus(OrderStatus.PAID)
                .build();

        ReflectionTestUtils.setField(order, "id", 1L);

        given(orderRepository.findById(1L)).willReturn(Optional.of(order));

        // when
        orderService.cancelOrderByAdmin(1L);

        // then
        verify(paymentService).cancelPayment(1L);
        assertEquals(OrderStatus.CANCELLED, order.getOrderStatus());
    }

    @Test
    void 관리자_주문_취소_실패_테스트() {
        // given
        Order order = Order.builder()
                .user(null)
                .orderNumber(1L)
                .totalAmount(8000L)
                .orderType(OrderType.KIOSK)
                .orderStatus(OrderStatus.CANCELLED)
                .build();

        ReflectionTestUtils.setField(order, "id", 1L);

        given(orderRepository.findById(1L)).willReturn(Optional.of(order));

        // when & then
        assertThrows(OrderException.class, () -> orderService.cancelOrderByAdmin(1L));
    }
}
