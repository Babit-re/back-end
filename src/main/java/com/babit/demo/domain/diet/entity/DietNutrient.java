package com.babit.demo.domain.diet.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "diet_nutrients")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class DietNutrient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diet_id", nullable = false)
    private Diet diet;

    private String nutrientName;

    private double amount;

    private String unit;
}
