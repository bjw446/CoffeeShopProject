package com.example.coffee_shop_project.auth.service;

import com.example.coffee_shop_project.auth.dto.AuthResponse;
import com.example.coffee_shop_project.auth.dto.RegisterRequest;
import com.example.coffee_shop_project.domain.user.entity.User;
import com.example.coffee_shop_project.domain.user.enums.UserRole;
import com.example.coffee_shop_project.domain.user.enums.UserStatus;
import com.example.coffee_shop_project.domain.user.exception.UserException;
import com.example.coffee_shop_project.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void 회원_가입_성공_테스트() {
        // given
        RegisterRequest request = new RegisterRequest();
        ReflectionTestUtils.setField(request, "name", "테스트");
        ReflectionTestUtils.setField(request, "email", "test@test.com");
        ReflectionTestUtils.setField(request, "password", "12345678");
        ReflectionTestUtils.setField(request, "phone", "010-1234-5678");

        given(userRepository.existsByEmail("test@test.com")).willReturn(false);
        given(passwordEncoder.encode("12345678")).willReturn("비밀번호암호화");

        User savedUser = User.builder()
                .name("테스트")
                .email("test@test.com")
                .password("비밀번호암호화")
                .phone("010-1234-5678")
                .membershipNumber("1234-5678-1234-5678")
                .point(0L)
                .userStatus(UserStatus.ACTIVE)
                .userRole(UserRole.USER)
                .build();

        given(userRepository.save(any(User.class))).willReturn(savedUser);

        // when
        AuthResponse response = authService.register(request);

        // then
        assertThat(response.getEmail()).isEqualTo("test@test.com");
        assertThat(response.getMembershipNumber()).isEqualTo("1234-5678-1234-5678");
    }

    @Test
    void 회원_가입_실패_이메일_중복_오류_테스트() {
        // given
        RegisterRequest request = new RegisterRequest();
        ReflectionTestUtils.setField(request, "name", "테스트");
        ReflectionTestUtils.setField(request, "email", "test@test.com");
        ReflectionTestUtils.setField(request, "password", "12345678");
        ReflectionTestUtils.setField(request, "phone", "010-1234-5678");

        given(userRepository.existsByEmail("test@test.com")).willReturn(true);

        // when & then
        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(UserException.class)
                .hasMessage("이미 사용 중인 이메일입니다");
    }
}
