package com.example.coffee_shop_project.domain.user.repository;

import com.example.coffee_shop_project.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
