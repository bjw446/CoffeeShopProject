package com.example.coffee_shop_project.common.config;

import com.example.coffee_shop_project.domain.menu.entity.Menu;
import com.example.coffee_shop_project.domain.menu.enums.Category;
import com.example.coffee_shop_project.domain.menu.repository.MenuRepository;
import com.example.coffee_shop_project.domain.user.entity.User;
import com.example.coffee_shop_project.domain.user.enums.UserRole;
import com.example.coffee_shop_project.domain.user.enums.UserStatus;
import com.example.coffee_shop_project.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Initializer implements CommandLineRunner {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;

    @Override
    public void run(String... args) throws Exception {
        User user = User.builder()
                .name("ADMIN")
                .email("admin@test.com")
                .password(passwordEncoder.encode("12345678"))
                .phone("010-1234-1234")
                .membershipNumber(MembershipNumberGenerator.generateMembershipNumber())
                .point(5000L)
                .userStatus(UserStatus.ACTIVE)
                .userRole(UserRole.ADMIN)
                .build();

        userRepository.save(user);

        User user2 = User.builder()
                .name("USER")
                .email("test@test.com")
                .password(passwordEncoder.encode("12345678"))
                .phone("010-1234-5678")
                .membershipNumber(MembershipNumberGenerator.generateMembershipNumber())
                .point(15000L)
                .userStatus(UserStatus.ACTIVE)
                .userRole(UserRole.USER)
                .build();

        userRepository.save(user2);

        Menu menu = Menu.builder()
                .name("아메리카노")
                .price(3000L)
                .category(Category.COFFEE)
                .build();

        menuRepository.save(menu);

        Menu menu2 = Menu.builder()
                .name("카페라떼")
                .price(4000L)
                .category(Category.LATTE)
                .build();

        menuRepository.save(menu2);

        Menu menu3 = Menu.builder()
                .name("딸기 스무디")
                .price(5500L)
                .category(Category.SMOOTHIE)
                .build();

        menuRepository.save(menu3);
    }
}
