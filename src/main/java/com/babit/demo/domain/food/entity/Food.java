package com.babit.demo.domain.food.entity;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "foods")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; //JPA에서 Integer 사용권장(Optional, 컬렉션 사용 가능, Null값 가능)

    @Column(nullable = false)
    private String name;

    @Column(name = "rep_name", nullable = false)
    private String repName;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false, columnDefinition = "FLOAT DEFAULT 0")
    private float calory;

    @Column(nullable = false, columnDefinition = "FLOAT DEFAULT 0")
    private float carbs;

    @Column(nullable = false, columnDefinition = "FLOAT DEFAULT 0")
    private float protein;

    @Column(nullable = false, columnDefinition = "FLOAT DEFAULT 0")
    private float fat;

    @Column(nullable = false, columnDefinition = "FLOAT DEFAULT 0")
    private float sodium;

    @Column(nullable = false, columnDefinition = "FLOAT DEFAULT 0")
    private float sugar;

    @Column(nullable = false, columnDefinition = "FLOAT DEFAULT 0")
    private float fiber;

    @Column(name = "saturated_fat", nullable = false, columnDefinition = "FLOAT DEFAULT 0")
    private float saturatedFat;
}
