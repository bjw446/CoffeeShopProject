package com.example.coffee_shop_project.auth.controller;

import com.example.coffee_shop_project.auth.dto.AuthResponse;
import com.example.coffee_shop_project.auth.dto.RegisterRequest;
import com.example.coffee_shop_project.auth.service.AuthService;
import com.example.coffee_shop_project.common.dto.CommonResponse;
import com.example.coffee_shop_project.common.enums.SuccessStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<CommonResponse<AuthResponse>> register (@Valid @RequestBody RegisterRequest request, HttpMethod httpMethod) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                CommonResponse.success(SuccessStatus.REGISTER_SUCCESS, authService.register(request))
        );
    }
}
