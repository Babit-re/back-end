package com.babit.demo.domain.diet.entity;

import com.babit.demo.domain.user.type.Gender;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "standard_nutrients")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class StandardNutrient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nutrientName;

    private double minAmount;

    private double maxAmount;

    @Column(nullable = false, length = 20)
    private String unit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

}
