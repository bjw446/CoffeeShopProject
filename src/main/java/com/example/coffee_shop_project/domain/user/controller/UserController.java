package com.example.coffee_shop_project.domain.user.controller;

import com.example.coffee_shop_project.common.dto.CommonResponse;
import com.example.coffee_shop_project.common.enums.SuccessStatus;
import com.example.coffee_shop_project.domain.user.dto.MembershipRequest;
import com.example.coffee_shop_project.domain.user.dto.MembershipResponse;
import com.example.coffee_shop_project.domain.user.dto.ChargePointRequest;
import com.example.coffee_shop_project.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/membership")
    public ResponseEntity<CommonResponse<MembershipResponse>> membershipLookup (@Valid @RequestBody MembershipRequest request) {
        return ResponseEntity.ok(CommonResponse.success(SuccessStatus.READ_SUCCESS, userService.membershipLookup(request)));
    }

    @PostMapping("/points/charge")
    public ResponseEntity<CommonResponse<MembershipResponse>> chargePoint (@Valid @RequestBody ChargePointRequest request) {
        return ResponseEntity.ok(CommonResponse.success(SuccessStatus.CHARGE_SUCCESS, userService.chargePoint(request)));
    }
}
