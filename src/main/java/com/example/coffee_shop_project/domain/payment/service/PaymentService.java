package com.example.coffee_shop_project.domain.payment.service;

import com.example.coffee_shop_project.common.enums.ErrorStatus;
import com.example.coffee_shop_project.domain.payment.entity.Payment;
import com.example.coffee_shop_project.domain.payment.exception.PaymentException;
import com.example.coffee_shop_project.domain.payment.repository.PaymentRepository;
import com.example.coffee_shop_project.domain.pointhistory.entity.PointHistory;
import com.example.coffee_shop_project.domain.pointhistory.enums.PointType;
import com.example.coffee_shop_project.domain.pointhistory.repository.PointHistoryRepository;
import com.example.coffee_shop_project.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PointHistoryRepository pointHistoryRepository;

    public void cancelPayment(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(
                () -> new PaymentException(ErrorStatus.PAYMENT_NOT_FOUND)
        );

        payment.cancel();

        User user = payment.getUser();

        user.refundPoint(payment.getAmount().longValue());

        pointHistoryRepository.save(
                PointHistory.builder()
                        .user(user)
                        .point(payment.getAmount().longValue())
                        .pointType(PointType.REFUND)
                        .build()
        );
    }
}
