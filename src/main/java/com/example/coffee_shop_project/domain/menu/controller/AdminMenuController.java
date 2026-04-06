package com.example.coffee_shop_project.domain.menu.controller;

import com.example.coffee_shop_project.common.dto.CommonResponse;
import com.example.coffee_shop_project.common.enums.SuccessStatus;
import com.example.coffee_shop_project.domain.menu.dto.CreateMenuRequest;
import com.example.coffee_shop_project.domain.menu.dto.MenuResponse;
import com.example.coffee_shop_project.domain.menu.service.MenuService;
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
@RequestMapping("/admin/menus")
public class AdminMenuController {
    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<CommonResponse<MenuResponse>> createMenu (@Valid @RequestBody CreateMenuRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.success(SuccessStatus.CREATE_SUCCESS, menuService.createMenu(request)));
    }
}
