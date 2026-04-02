package com.example.coffee_shop_project.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus {
    LOGIN_FAILED(HttpStatus.BAD_REQUEST, "400_LOGIN_FAILED", "이메일 또는 비밀번호가 올바르지 않습니다"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "401_INVALID_TOKEN", "유효하지 않은 토큰입니다"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "401_TOKEN_EXPIRED", "토큰이 만료되었습니다"),
    INVALID_SIGNATURE(HttpStatus.UNAUTHORIZED, "401_INVALID_SIGNATURE", "토큰 서명이 유효하지 않습니다"),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "400_DUPLICATE_EMAIL", "이미 사용 중인 이메일입니다"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "404_USER_NOT_FOUND", "존재하지 않는 유저입니다"),
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "404_MENU_NOT_FOUND", "존재하지 않는 메뉴입니다"),
    ADMIN_NOT_FOUND(HttpStatus.NOT_FOUND, "404_ADMIN_NOT_FOUND", "관리자가 존재하지 않습니다"),
    ORDER_ITEMS_NOT_FOUND(HttpStatus.NOT_FOUND, "404_ORDER_ITEMS_NOT_FOUND", "주문 상품이 존재하지 않습니다"),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "404_ORDER_NOT_FOUND", "주문이 존재하지 않습니다"),
    EVENT_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "404_EVENT_TYPE_NOT_FOUND", "이벤트 타입이 존재하지 않습니다"),
    EVENT_NOT_FOUND(HttpStatus.NOT_FOUND, "404_EVENT_NOT_FOUND", "이벤트가 존재하지 않습니다"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "400_INVALID_PASSWORD", "비밀번호가 일치하지 않습니다"),
    ALREADY_DELETED_USER(HttpStatus.GONE, "410_ALREADY_DELETED_USER", "이미 탈퇴한 회원 입니다"),
    ALREADY_CANCELLED_ORDER(HttpStatus.GONE, "410_ALREADY_CANCELLED_ORDER", "이미 취소한 주문 입니다"),
    ALREADY_PAID_ORDER(HttpStatus.GONE, "410_ALREADY_PAID_ORDER", "이미 결제 완료한 주문 입니다"),
    ALREADY_CANCELLED_PAYMENT(HttpStatus.GONE, "410_ALREADY_CANCELLED_PAYMENT", "이미 취소한 결제 입니다"),
    FAILED_PAYMENT(HttpStatus.BAD_REQUEST, "400_FAILED_PAYMENT", "이미 실패한 결제 입니다"),
    ACCESS_FORBIDDEN(HttpStatus.FORBIDDEN, "403_ACCESS_FORBIDDEN", "권한이 부족합니다"),
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "404_PAYMENT_NOT_FOUND", "존재하지 않는 결제입니다"),
    MEMBER_ONLY_USE_POINT(HttpStatus.BAD_REQUEST, "400_MEMBER_ONLY_USE_POINT", "비회원은 포인트 결제를 할 수 없습니다"),
    INSUFFICIENT_POINT(HttpStatus.BAD_REQUEST, "400_INSUFFICIENT_POINT", "잔액이 부족합니다"),
    LOCK_ACQUISITION_FAILED(HttpStatus.CONFLICT, "409_LOCK_ACQUISITION_FAILED", "다른 사용자가 이미 요청 중입니다"),
    LOCK_TIMEOUT(HttpStatus.REQUEST_TIMEOUT, "408_LOCK_TIMEOUT", "요청이 많아 처리가 지연되었습니다. 잠시 후 다시 시도해주세요"),
    LOCK_RELEASE_FAILED(HttpStatus.BAD_REQUEST, "400_LOCK_RELEASE_FAILED", "요청 처리 중 문제가 발생했습니다. 다시 시도해주세요"),
    LOCK_INTERRUPTED(HttpStatus.INTERNAL_SERVER_ERROR, "500_LOCK_INTERRUPTED", "요청이 정상적으로 처리되지 않았습니다. 잠시 후 다시 시도해주세요");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}