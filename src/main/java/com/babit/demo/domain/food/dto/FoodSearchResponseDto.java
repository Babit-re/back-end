package com.babit.demo.domain.food.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FoodSearchResponseDto {
    private Long foodId;
    private String name;
    private double calorie;
    private double quantity;
}