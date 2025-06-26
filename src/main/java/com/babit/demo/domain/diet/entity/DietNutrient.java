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

    @Column(nullable = false)
    private String nutrientName;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false, length = 20)
    private String unit;
}
