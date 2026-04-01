package com.example.coffee_shop_project.auth.service;

import com.example.coffee_shop_project.auth.dto.AuthResponse;
import com.example.coffee_shop_project.auth.dto.RegisterRequest;
import com.example.coffee_shop_project.common.config.MembershipNumberGenerator;
import com.example.coffee_shop_project.common.enums.ErrorStatus;
import com.example.coffee_shop_project.domain.user.entity.User;
import com.example.coffee_shop_project.domain.user.enums.UserRole;
import com.example.coffee_shop_project.domain.user.enums.UserStatus;
import com.example.coffee_shop_project.domain.user.exception.UserException;
import com.example.coffee_shop_project.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse register(RegisterRequest request) {
        boolean existence = userRepository.existsByEmail(request.getEmail());
        if (existence) {
            throw new UserException(ErrorStatus.DUPLICATE_EMAIL);
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .membershipNumber(uniqueMembershipNumber())
                .point(0L)
                .userStatus(UserStatus.ACTIVE)
                .userRole(UserRole.USER)
                .build();

        User savedUser = userRepository.save(user);

        return AuthResponse.from(savedUser);
    }

    private String uniqueMembershipNumber() {
        String membershipNumber;
        do {
            membershipNumber = MembershipNumberGenerator.generateMembershipNumber();
        } while (userRepository.existsByMembershipNumber(membershipNumber));
        return membershipNumber;
    }
}
