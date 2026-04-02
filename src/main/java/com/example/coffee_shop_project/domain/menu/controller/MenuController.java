package com.example.coffee_shop_project.domain.menu.controller;

import com.example.coffee_shop_project.common.dto.CommonResponse;
import com.example.coffee_shop_project.common.enums.SuccessStatus;
import com.example.coffee_shop_project.domain.menu.dto.MenuResponse;
import com.example.coffee_shop_project.domain.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/menus")
public class MenuController {
    private final MenuService menuService;

    @GetMapping
    public ResponseEntity<CommonResponse<Page<MenuResponse>>> getAllMenu(
            @PageableDefault Pageable pageable,
            @RequestParam(defaultValue = "1") int page
    ) {
        Pageable converted = PageRequest.of(
                page - 1,
                pageable.getPageSize(),
                pageable.getSort()
        );

        return ResponseEntity.ok(CommonResponse.success(SuccessStatus.READ_SUCCESS, menuService.findAllMenu(converted)));
    }

    @GetMapping("/{menuId}")
    public ResponseEntity<CommonResponse<MenuResponse>> getOneMenu(@PathVariable Long menuId) {
        return ResponseEntity.ok(CommonResponse.success(SuccessStatus.READ_SUCCESS, menuService.findOneMenu(menuId)));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<CommonResponse<Page<MenuResponse>>> getMenuByCategory(
            @PathVariable String category,
            @PageableDefault Pageable pageable,
            @RequestParam(defaultValue = "1") int page
    ) {
        Pageable converted = PageRequest.of(
                page - 1,
                pageable.getPageSize(),
                pageable.getSort()
        );

        return ResponseEntity.ok(CommonResponse.success(SuccessStatus.READ_SUCCESS, menuService.findMenuByCategory(category, converted)));
    }

    @GetMapping("/popular")
    public ResponseEntity<CommonResponse<List<MenuResponse>>> getPopularMenu() {
        return ResponseEntity.ok(CommonResponse.success(SuccessStatus.READ_SUCCESS, menuService.findPopularMenu()));
    }
}
