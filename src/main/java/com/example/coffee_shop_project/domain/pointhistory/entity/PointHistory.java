package com.example.coffee_shop_project.domain.pointhistory.entity;

import com.example.coffee_shop_project.common.entity.DeletableEntity;
import com.example.coffee_shop_project.domain.pointhistory.enums.PointType;
import com.example.coffee_shop_project.domain.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "point_histories")
public class PointHistory extends DeletableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    private Long point;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PointType pointType;

    @Builder
    public PointHistory(User user, Long point, PointType pointType) {
        this.user = user;
        this.point = point;
        this.pointType = pointType;
    }
}
