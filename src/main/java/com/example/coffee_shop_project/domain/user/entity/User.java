package com.example.coffee_shop_project.domain.user.entity;

import com.example.coffee_shop_project.common.entity.DeletableEntity;
import com.example.coffee_shop_project.domain.user.enums.UserRole;
import com.example.coffee_shop_project.domain.user.enums.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.Random;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends DeletableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Length(max = 30)
    private String name;

    @NotBlank
    @Length(max = 50)
    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    @Length(max = 255)
    private String password;

    @NotBlank
    @Pattern(regexp = "^010-[\\d*]{4}-\\d{4}$")
    @Column(unique = true)
    private String phone;

    @NotBlank
    @Length(max = 20)
    @Column(unique = true)
    private String membershipNumber;

    @NotNull
    private Long point;

    @NotNull
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @NotNull
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Builder
    public User (String name, String email, String password, String phone, String membershipNumber, Long point, UserStatus userStatus, UserRole userRole) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.membershipNumber = membershipNumber;
        this.point = point;
        this.userStatus = userStatus;
        this.userRole = userRole;
    }

    public void refundPoint(Long amount) {
        this.point += amount;
    }
}
