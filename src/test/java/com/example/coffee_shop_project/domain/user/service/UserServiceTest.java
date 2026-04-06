package com.example.coffee_shop_project.domain.user.service;

import com.example.coffee_shop_project.domain.user.dto.ChargePointRequest;
import com.example.coffee_shop_project.domain.user.dto.MembershipRequest;
import com.example.coffee_shop_project.domain.user.dto.MembershipResponse;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;


    @Test
    void 멤버쉽_검증_성공_테스트() {
        // given
        MembershipRequest request = new MembershipRequest();
        ReflectionTestUtils.setField(request, "membershipNumber", "1234-5678-1234-5678");

        User user = User.builder()
                .name("테스트")
                .email("test@test.com")
                .password("12345678")
                .phone("010-1234-5678")
                .membershipNumber("1234-5678-1234-5678")
                .point(200L)
                .userStatus(UserStatus.ACTIVE)
                .userRole(UserRole.USER)
                .build();

        given(userRepository.findByMembershipNumber("1234-5678-1234-5678"))
                .willReturn(Optional.of(user));

        // when
        MembershipResponse response = userService.membershipLookup(request);

        // then
        assertThat(response.isMember()).isTrue();
        assertThat(response.getName()).isEqualTo("테스트");
        assertThat(response.getPoint()).isEqualTo(200L);
    }

    @Test
    void 멤버쉽_검증_실패_테스트() {
        // given
        MembershipRequest request = new MembershipRequest();
        ReflectionTestUtils.setField(request, "membershipNumber", "");

        given(userRepository.findByMembershipNumber(""))
                .willReturn(Optional.empty());

        // when
        MembershipResponse response = userService.membershipLookup(request);

        // then
        assertThat(response.isMember()).isFalse();
    }

    @Test
    void 포인트_충전_성공_테스트() {
        // given
        ChargePointRequest request = new ChargePointRequest();
        ReflectionTestUtils.setField(request, "membershipNumber", "1234-1234-1234-1234");
        ReflectionTestUtils.setField(request, "point", 5000L);

        User user = User.builder()
                .name("테스트")
                .email("test@test.com")
                .password("12345678")
                .phone("010-1234-5678")
                .membershipNumber("1234-1234-1234-1234")
                .point(200L)
                .userStatus(UserStatus.ACTIVE)
                .userRole(UserRole.USER)
                .build();

        given(userRepository.findByMembershipNumber("1234-1234-1234-1234"))
                .willReturn(Optional.of(user));

        // when
        MembershipResponse response = userService.chargePoint(request);

        // then
        assertEquals(true, response.isMember());
        assertEquals(5200L, response.getPoint());
    }

    @Test
    void 포인트_충전_실패_테스트() {
        // given
        ChargePointRequest request = new ChargePointRequest();
        ReflectionTestUtils.setField(request, "membershipNumber", "1234-1234-1234-1234");
        ReflectionTestUtils.setField(request, "point", 5000L);

        // when & then
        assertThrows(UserException.class, () -> userService.chargePoint(request));

    }
}
