package com.babit.demo.domain.diet.entity;

import com.babit.demo.domain.diet.type.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.*;
import java.util.List;

@Entity
@Table(name = "diets")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Diet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private LocalDate date;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime time;

    @Enumerated(EnumType.STRING)
    private DietType type;

    @Enumerated(EnumType.STRING)
    private SatisfactionType satisfaction;

    private String memo;

    private double totalCalorie;

    @OneToMany(mappedBy = "diet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DietFood> foods;

    @OneToMany(mappedBy = "diet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DietNutrient> nutrients;

}
