package com.example.coffee_shop_project.auth.controller;

import com.example.coffee_shop_project.auth.dto.AuthResponse;
import com.example.coffee_shop_project.auth.dto.RegisterRequest;
import com.example.coffee_shop_project.auth.service.AuthService;
import com.example.coffee_shop_project.common.enums.ErrorStatus;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@MockBean(JpaMetamodelMappingContext.class)
@Import(SecurityConfig.class)
public class AuthControllerTest {

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void 회원_가입_성공_테스트() throws Exception {
        // given
        String requestBody = """
                {
                    "name": "테스트",
                    "email": "test@test.com",
                    "password": "12345678",
                    "phone": "010-1234-5678"
                }
                """;

        AuthResponse response = AuthResponse.builder()
                .id(1L)
                .name("테스트")
                .email("test@test.com")
                .phone("010-1234-5678")
                .membershipNumber("1234-5678-1234-5678")
                .point(0L)
                .role("USER")
                .status("ACTIVE")
                .build();

        given(authService.register(any(RegisterRequest.class))).willReturn(response);

        // when & then
        mockMvc.perform(post("/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("201_REGISTER_SUCCESS"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value("테스트"))
                .andExpect(jsonPath("$.data.email").value("test@test.com"))
                .andExpect(jsonPath("$.data.phone").value("010-1234-5678"))
                .andExpect(jsonPath("$.data.membershipNumber").value("1234-5678-1234-5678"))
                .andExpect(jsonPath("$.data.point").value(0L))
                .andExpect(jsonPath("$.data.role").value("USER"))
                .andExpect(jsonPath("$.data.status").value("ACTIVE"));
    }

    @Test
    void 회원_가입_실패_이메일_중복_오류_테스트() throws Exception {
        // given
        String requestBody = """
            {
                "name": "테스트",
                "email": "test@test.com",
                "password": "12345678",
                "phone": "010-1234-5678"
            }
            """;

        given(authService.register(any(RegisterRequest.class)))
                .willThrow(new UserException(ErrorStatus.DUPLICATE_EMAIL));

        // when & then
        mockMvc.perform(post("/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400_DUPLICATE_EMAIL"))
                .andExpect(jsonPath("$.message").value("이미 사용 중인 이메일입니다"))
                .andExpect(jsonPath("$.path").value("/auth/register"));
    }
}
