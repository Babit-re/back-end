package com.babit.demo.domain.diet.entity;

import com.babit.demo.domain.diet.type.GenderType;
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

    private String nutrientName;

    private double minAmount;

    private double maxAmount;

    private String unit;

    @Enumerated(EnumType.STRING)
    private GenderType gender;

}
