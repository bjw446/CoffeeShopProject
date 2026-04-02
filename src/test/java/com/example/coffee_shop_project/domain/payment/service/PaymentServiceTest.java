package com.example.coffee_shop_project.domain.payment.service;

import com.example.coffee_shop_project.domain.order.entity.Order;
import com.example.coffee_shop_project.domain.order.enums.OrderStatus;
import com.example.coffee_shop_project.domain.order.enums.OrderType;
import com.example.coffee_shop_project.domain.order.service.OrderService;
import com.example.coffee_shop_project.domain.payment.entity.Payment;
import com.example.coffee_shop_project.domain.payment.enums.PayType;
import com.example.coffee_shop_project.domain.payment.enums.PaymentStatus;
import com.example.coffee_shop_project.domain.payment.exception.PaymentException;
import com.example.coffee_shop_project.domain.payment.repository.PaymentRepository;
import com.example.coffee_shop_project.domain.pointhistory.entity.PointHistory;
import com.example.coffee_shop_project.domain.pointhistory.repository.PointHistoryRepository;
import com.example.coffee_shop_project.domain.user.entity.User;
import com.example.coffee_shop_project.domain.user.enums.UserRole;
import com.example.coffee_shop_project.domain.user.enums.UserStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {
    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private OrderService orderService;

    @Mock
    private PointHistoryRepository pointHistoryRepository;

    @Test
    void 비회원_결제_취소_성공_테스트() {
        // given
        Order order = Order.builder()
                .user(null)
                .orderNumber(1L)
                .totalAmount(8000L)
                .orderType(OrderType.KIOSK)
                .orderStatus(OrderStatus.PAID)
                .build();

        ReflectionTestUtils.setField(order, "id", 1L);

        Payment payment = Payment.builder()
                .user(null)
                .order(order)
                .amount(BigDecimal.valueOf(8000))
                .payType(PayType.POINT)
                .paymentStatus(PaymentStatus.SUCCESS)
                .build();

        ReflectionTestUtils.setField(payment, "id", 1L);

        given(paymentRepository.findByOrderId(order.getId())).willReturn(Optional.of(payment));

        // when
        paymentService.cancelPayment(payment.getOrder().getId());

        // then
        assertEquals(PaymentStatus.CANCELLED, payment.getPaymentStatus());
    }

    @Test
    void 결제_취소_성공_테스트() {
        // given
        User user = User.builder()
                .name("테스트")
                .email("test@test.com")
                .password("12345678")
                .phone("010-1234-5678")
                .membershipNumber("1234-5678-1234-5678")
                .point(200L)
                .userStatus(UserStatus.ACTIVE)
                .userRole(UserRole.USER)
                .build();

        ReflectionTestUtils.setField(user, "id", 1L);

        Order order = Order.builder()
                .user(user)
                .orderNumber(1L)
                .totalAmount(8000L)
                .orderType(OrderType.KIOSK)
                .orderStatus(OrderStatus.PAID)
                .build();

        ReflectionTestUtils.setField(order, "id", 1L);

        Payment payment = Payment.builder()
                .user(user)
                .order(order)
                .amount(BigDecimal.valueOf(8000))
                .payType(PayType.POINT)
                .paymentStatus(PaymentStatus.SUCCESS)
                .build();

        ReflectionTestUtils.setField(payment, "id", 1L);



        given(paymentRepository.findByOrderId(order.getId())).willReturn(Optional.of(payment));

        // when
        paymentService.cancelPayment(payment.getOrder().getId());

        // then
        verify(pointHistoryRepository).save(any(PointHistory.class));
        assertEquals(PaymentStatus.CANCELLED, payment.getPaymentStatus());
        assertEquals(8200L, user.getPoint());
    }

    @Test
    void 결제_취소_실패_테스트() {
        // given
        User user = User.builder()
                .name("테스트")
                .email("test@test.com")
                .password("12345678")
                .phone("010-1234-5678")
                .membershipNumber("1234-5678-1234-5678")
                .point(200L)
                .userStatus(UserStatus.ACTIVE)
                .userRole(UserRole.USER)
                .build();

        ReflectionTestUtils.setField(user, "id", 1L);

        Order order = Order.builder()
                .user(user)
                .orderNumber(1L)
                .totalAmount(8000L)
                .orderType(OrderType.KIOSK)
                .orderStatus(OrderStatus.PAID)
                .build();

        ReflectionTestUtils.setField(order, "id", 1L);

        Payment payment = Payment.builder()
                .user(user)
                .order(order)
                .amount(BigDecimal.valueOf(8000))
                .payType(PayType.POINT)
                .paymentStatus(PaymentStatus.CANCELLED)
                .build();

        ReflectionTestUtils.setField(payment, "id", 1L);

        given(paymentRepository.findByOrderId(order.getId())).willReturn(Optional.of(payment));

        // when & then
        assertThrows(PaymentException.class, () -> paymentService.cancelPayment(1L));

    }
}
