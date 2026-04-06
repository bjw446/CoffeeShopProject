package com.example.coffee_shop_project.domain.user.repository;

import com.example.coffee_shop_project.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    boolean existsByMembershipNumber(String membershipNumber);

    Optional<User> findByMembershipNumber(String membershipNumber);
}
