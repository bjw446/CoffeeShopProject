package com.example.coffee_shop_project.domain.user.dto;

import com.example.coffee_shop_project.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MembershipResponse {
    private final boolean isMember;
    private final String name;
    private final Long point;

    public static MembershipResponse isMember(User user) {
        return MembershipResponse.builder()
                .isMember(true)
                .name(user.getName())
                .point(user.getPoint())
                .build();
    }

    public static MembershipResponse nonMember() {
        return MembershipResponse.builder()
                .isMember(false)
                .build();
    }
}
