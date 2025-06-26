package com.babit.demo.domain.diet.entity;

import com.babit.demo.domain.diet.type.*;
import com.babit.demo.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.*;
import java.util.*;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate date;

    @JsonFormat(pattern = "HH:mm")
    @Column(nullable = false)
    private LocalTime time;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DietType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SatisfactionType satisfaction;

    private String memo;

    @Column(nullable = false)
    private double totalCalorie;

    @Builder.Default
    @OneToMany(mappedBy = "diet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DietFood> foods = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "diet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DietNutrient> nutrients = new ArrayList<>();

}
