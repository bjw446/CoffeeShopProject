package com.example.coffee_shop_project.domain.user.controller;

import com.example.coffee_shop_project.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
}
