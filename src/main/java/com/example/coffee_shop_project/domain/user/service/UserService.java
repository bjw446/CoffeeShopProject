package com.example.coffee_shop_project.domain.user.service;

import com.example.coffee_shop_project.common.enums.ErrorStatus;
import com.example.coffee_shop_project.domain.user.dto.MembershipRequest;
import com.example.coffee_shop_project.domain.user.dto.MembershipResponse;
import com.example.coffee_shop_project.domain.user.dto.ChargePointRequest;
import com.example.coffee_shop_project.domain.user.entity.User;
import com.example.coffee_shop_project.domain.user.exception.UserException;
import com.example.coffee_shop_project.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public MembershipResponse membershipLookup(MembershipRequest request) {
        Optional<User> user = Optional.empty();

        if (request.getMembershipNumber() != null) {
            user = userRepository.findByMembershipNumber(request.getMembershipNumber());
        }

        if (user.isEmpty()) {
            return MembershipResponse.nonMember();
        }

        User existsUser = user.get();

        return MembershipResponse.isMember(existsUser);
    }

    public MembershipResponse chargePoint(ChargePointRequest request) {
        User user = userRepository.findByMembershipNumber(request.getMembershipNumber()).orElseThrow(
                () -> new UserException(ErrorStatus.USER_NOT_FOUND)
        );

        user.updatePoint(request.getPoint());

        return MembershipResponse.isMember(user);
    }
}
