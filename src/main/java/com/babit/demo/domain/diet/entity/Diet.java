package com.babit.demo.domain.diet.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Diet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
