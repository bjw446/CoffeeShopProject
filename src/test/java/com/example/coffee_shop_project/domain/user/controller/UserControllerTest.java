package com.example.coffee_shop_project.domain.user.controller;

import com.example.coffee_shop_project.domain.user.dto.MembershipRequest;
import com.example.coffee_shop_project.domain.user.dto.MembershipResponse;
import com.example.coffee_shop_project.domain.user.service.UserService;
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

@WebMvcTest(UserController.class)
@MockBean(JpaMetamodelMappingContext.class)
@Import(SecurityConfig.class)
public class UserControllerTest {

    @MockBean
    private UserService userService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void 멤버쉽_검증_성공_테스트() throws Exception {
        // given
        String requestBody = """
                {
                    "membershipNumber": "1234-5678-1234-5678"
                }
                """;

        MembershipResponse response = MembershipResponse.builder()
                .isMember(true)
                .name("테스트")
                .point(200L)
                .build();

        given(userService.membershipLookup(any(MembershipRequest.class))).willReturn(response);

        // when & then
        mockMvc.perform(post("/users/membership")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200_READ_SUCCESS"))
                .andExpect(jsonPath("$.data.member").value(true))
                .andExpect(jsonPath("$.data.name").value("테스트"))
                .andExpect(jsonPath("$.data.point").value(200L));
    }

    @Test
    void 멤버쉽_검증_실패_테스트() throws Exception {
        // given
        String requestBody = """
                {
                    "membershipNumber": ""
                }
                """;

        MembershipResponse response = MembershipResponse.builder()
                .isMember(false)
                .name(null)
                .point(null)
                .build();

        given(userService.membershipLookup(any(MembershipRequest.class))).willReturn(response);

        // when & then
        mockMvc.perform(post("/users/membership")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200_READ_SUCCESS"))
                .andExpect(jsonPath("$.data.member").value(false))
                .andExpect(jsonPath("$.data.name").isEmpty())
                .andExpect(jsonPath("$.data.point").isEmpty());
    }
}
