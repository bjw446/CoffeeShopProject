package com.example.coffee_shop_project.domain.payment.service;

import com.example.coffee_shop_project.common.enums.ErrorStatus;
import com.example.coffee_shop_project.domain.order.entity.Order;
import com.example.coffee_shop_project.domain.order.exception.OrderException;
import com.example.coffee_shop_project.domain.order.repository.OrderRepository;
import com.example.coffee_shop_project.domain.payment.dto.CreatePaymentRequest;
import com.example.coffee_shop_project.domain.payment.dto.PaymentResponse;
import com.example.coffee_shop_project.domain.payment.entity.Payment;
import com.example.coffee_shop_project.domain.payment.enums.PayType;
import com.example.coffee_shop_project.domain.payment.enums.PaymentStatus;
import com.example.coffee_shop_project.domain.payment.exception.PaymentException;
import com.example.coffee_shop_project.domain.payment.repository.PaymentRepository;
import com.example.coffee_shop_project.domain.pointhistory.entity.PointHistory;
import com.example.coffee_shop_project.domain.pointhistory.enums.PointType;
import com.example.coffee_shop_project.domain.pointhistory.repository.PointHistoryRepository;
import com.example.coffee_shop_project.domain.user.entity.User;
import com.example.coffee_shop_project.domain.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final OrderRepository orderRepository;

    public PaymentResponse createPayment(CreatePaymentRequest request) {
        Order order = orderRepository.findById(request.getOrderId()).orElseThrow(
                () -> new OrderException(ErrorStatus.ORDER_NOT_FOUND)
        );

        if (order.getUser() == null && request.getPayType() == PayType.POINT) {
            throw new UserException(ErrorStatus.MEMBER_ONLY_USE_POINT);
        }

        if (order.getUser() != null && request.getPayType() == PayType.POINT) {
            User user = order.getUser();

            user.usePoint(order.getTotalAmount());

            pointHistoryRepository.save(
                    PointHistory.builder()
                            .user(user)
                            .point(order.getTotalAmount())
                            .pointType(PointType.USE)
                            .build()
            );
        }

        order.paid();

        Payment payment = Payment.builder()
                .user(order.getUser())
                .order(order)
                .amount(BigDecimal.valueOf(order.getTotalAmount()))
                .payType(request.getPayType())
                .paymentStatus(PaymentStatus.SUCCESS)
                .build();

        Payment savedPayment = paymentRepository.save(payment);
        return PaymentResponse.from(savedPayment);
    }

    public void cancelPayment(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(
                () -> new PaymentException(ErrorStatus.PAYMENT_NOT_FOUND)
        );

        payment.cancel();

        if (payment.getUser() != null) {
            User user = payment.getUser();

            user.updatePoint(payment.getAmount().longValue());

            pointHistoryRepository.save(
                    PointHistory.builder()
                            .user(user)
                            .point(payment.getAmount().longValue())
                            .pointType(PointType.REFUND)
                            .build()
            );
        }
    }
}
