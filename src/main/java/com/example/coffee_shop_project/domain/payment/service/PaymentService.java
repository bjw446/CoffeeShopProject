package com.example.coffee_shop_project.domain.payment.service;

import com.example.coffee_shop_project.common.enums.ErrorStatus;
import com.example.coffee_shop_project.domain.order.entity.Order;
import com.example.coffee_shop_project.domain.order.exception.OrderException;
import com.example.coffee_shop_project.domain.order.repository.OrderRepository;
import com.example.coffee_shop_project.domain.payment.dto.CreatePaymentRequest;
import com.example.coffee_shop_project.domain.payment.dto.PaymentResponse;
import com.example.coffee_shop_project.domain.payment.dto.PaymentSuccessEvent;
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
import com.example.coffee_shop_project.infra.outbox.entity.OutboxEvent;
import com.example.coffee_shop_project.infra.outbox.exception.EventCustomException;
import com.example.coffee_shop_project.infra.outbox.repository.OutboxRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final OrderRepository orderRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;
    private final RedissonClient redissonClient;

    public PaymentResponse createPayment(CreatePaymentRequest request) {
        Order order = orderRepository.findById(request.getOrderId()).orElseThrow(
                () -> new OrderException(ErrorStatus.ORDER_NOT_FOUND)
        );

        if (order.getUser() == null && request.getPayType() == PayType.POINT) {
            throw new UserException(ErrorStatus.MEMBER_ONLY_USE_POINT);
        }

        if (order.getUser() != null && request.getPayType() == PayType.POINT) {
            User user = order.getUser();

            RLock lock = redissonClient.getLock("lock:user:usePoint:" + user.getId());

            try {
                boolean available = lock.tryLock(5, 3, TimeUnit.SECONDS);

                if (!available) {
                    throw new PaymentException(ErrorStatus.LOCK_ACQUISITION_FAILED);
                }

                user.usePoint(order.getTotalAmount());

                order.paid();

                pointHistoryRepository.save(
                        PointHistory.builder()
                                .user(user)
                                .point(order.getTotalAmount())
                                .pointType(PointType.USE)
                                .build()
                );
            } catch (InterruptedException e) {
                throw new PaymentException(ErrorStatus.LOCK_INTERRUPTED);
            } finally {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        }

        Payment payment = Payment.builder()
                .user(order.getUser())
                .order(order)
                .amount(BigDecimal.valueOf(order.getTotalAmount()))
                .payType(request.getPayType())
                .paymentStatus(PaymentStatus.SUCCESS)
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        PaymentSuccessEvent event = new PaymentSuccessEvent(
                order.getId(),
                order.getUser() != null ? order.getUser().getId() : null,
                order.getTotalAmount()
        );

        String json;
        try {
            json = objectMapper.writeValueAsString(event);
        } catch (Exception e) {
            throw new EventCustomException(ErrorStatus.EVENT_NOT_FOUND);
        }

        outboxRepository.save(
                OutboxEvent.builder()
                        .aggregateType("PAYMENT")
                        .aggregateId(order.getId())
                        .eventType("PAYMENT_SUCCESS")
                        .eventKey(UUID.randomUUID().toString())
                        .payload(json)
                        .status("PENDING")
                        .build()
        );

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
