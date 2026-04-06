package com.example.coffee_shop_project.domain.payment.controller;

import com.example.coffee_shop_project.common.enums.ErrorStatus;
import com.example.coffee_shop_project.domain.payment.dto.CreatePaymentRequest;
import com.example.coffee_shop_project.domain.payment.dto.PaymentResponse;
import com.example.coffee_shop_project.domain.payment.enums.PayType;
import com.example.coffee_shop_project.domain.payment.enums.PaymentStatus;
import com.example.coffee_shop_project.domain.payment.service.PaymentService;
import com.example.coffee_shop_project.domain.user.exception.UserException;
import com.example.coffee_shop_project.security.SecurityConfig;
import com.example.coffee_shop_project.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
@MockBean(JpaMetamodelMappingContext.class)
@Import(SecurityConfig.class)
public class PaymentControllerTest {
    @MockBean
    private PaymentService paymentService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void 결제_생성_성공_테스트() throws Exception {
        // given
        String requestBody = """
                {
                    "userId": 1,
                    "orderId": 1,
                    "amount": 12000,
                    "payType": "POINT"
                }
                """;

        PaymentResponse response = PaymentResponse.builder()
                .id(1L)
                .userId(1L)
                .orderId(1L)
                .amount(BigDecimal.valueOf(12000))
                .payType(PayType.POINT)
                .paymentStatus(PaymentStatus.SUCCESS)
                .build();

        given(paymentService.createPayment(any(CreatePaymentRequest.class))).willReturn(response);

        // when & then
        mockMvc.perform(post("/payments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("201_CREATE_SUCCESS"))
                .andExpect(jsonPath("$.data.userId").value(1L))
                .andExpect(jsonPath("$.data.orderId").value(1L))
                .andExpect(jsonPath("$.data.amount").value(12000))
                .andExpect(jsonPath("$.data.payType").value("POINT"))
                .andExpect(jsonPath("$.data.paymentStatus").value("SUCCESS"));

    }

    @Test
    void 결제_생성_실패_테스트() throws Exception {
        // given
        String requestBody = """
                {
                    "userId": 1,
                    "orderId": 1,
                    "amount": 12000,
                    "payType": "POINT"
                }
                """;

        given(paymentService.createPayment(any(CreatePaymentRequest.class))).willThrow(new UserException(ErrorStatus.MEMBER_ONLY_USE_POINT));

        // when & then
        mockMvc.perform(post("/payments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400_MEMBER_ONLY_USE_POINT"))
                .andExpect(jsonPath("$.message").value("비회원은 포인트 결제를 할 수 없습니다"))
                .andExpect(jsonPath("$.path").value("/payments"));
    }
}
