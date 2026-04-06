package com.example.coffee_shop_project.domain.payment.controller;

import com.example.coffee_shop_project.common.dto.CommonResponse;
import com.example.coffee_shop_project.common.enums.SuccessStatus;
import com.example.coffee_shop_project.domain.payment.dto.CreatePaymentRequest;
import com.example.coffee_shop_project.domain.payment.dto.PaymentResponse;
import com.example.coffee_shop_project.domain.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<CommonResponse<PaymentResponse>> createPayment(@Valid @RequestBody CreatePaymentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.success(SuccessStatus.CREATE_SUCCESS, paymentService.createPayment(request)));
    }
}
