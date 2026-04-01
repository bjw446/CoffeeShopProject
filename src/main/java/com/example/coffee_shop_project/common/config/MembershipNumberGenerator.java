package com.example.coffee_shop_project.common.config;

import java.security.SecureRandom;

public class MembershipNumberGenerator {
    private static final SecureRandom random = new SecureRandom();

    public static String generateMembershipNumber() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 16; i++) {
            sb.append(random.nextInt(10));
            if ((i + 1) % 4 == 0 && i != 15) {
                sb.append("-");
            }
        }
        return sb.toString();
    }

}
