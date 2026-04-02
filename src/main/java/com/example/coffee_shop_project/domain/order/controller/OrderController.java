package com.example.coffee_shop_project.domain.order.controller;

import com.example.coffee_shop_project.common.dto.CommonResponse;
import com.example.coffee_shop_project.common.enums.SuccessStatus;
import com.example.coffee_shop_project.domain.order.dto.CreateOrderRequest;
import com.example.coffee_shop_project.domain.order.dto.OrderResponse;
import com.example.coffee_shop_project.domain.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

    @GetMapping("{orderId}")
    public ResponseEntity<CommonResponse<OrderResponse>> getOneOrder(@PathVariable Long orderId) {

        return ResponseEntity.ok(CommonResponse.success(SuccessStatus.READ_SUCCESS, orderService.findOneOrder(orderId)));
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<CommonResponse<Void>> cancelOrder(@PathVariable Long orderId) {

        orderService.cancelOrder(orderId);

        return ResponseEntity.ok(CommonResponse.success(SuccessStatus.UPDATE_SUCCESS, null));
    }
}
