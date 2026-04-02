package com.example.coffee_shop_project.domain.order.controller;

import com.example.coffee_shop_project.common.dto.CommonResponse;
import com.example.coffee_shop_project.common.enums.SuccessStatus;
import com.example.coffee_shop_project.domain.order.dto.CreateOrderRequest;
import com.example.coffee_shop_project.domain.order.dto.OrderResponse;
import com.example.coffee_shop_project.domain.order.service.OrderService;
import com.example.coffee_shop_project.domain.orderitems.entity.OrderItems;
import com.example.coffee_shop_project.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<CommonResponse<OrderResponse>> createOrder(@Valid @RequestBody CreateOrderRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.success(SuccessStatus.CREATE_SUCCESS, orderService.createOrder(request)));
    }
}
