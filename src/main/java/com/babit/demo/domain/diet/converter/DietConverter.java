package com.babit.demo.domain.diet.converter;

import com.babit.demo.domain.diet.dto.DietResponseDto;
import com.babit.demo.domain.diet.entity.Diet;

import java.util.List;

public class DietConverter {

    public static DietResponseDto toResponseDto(Diet diet) {
        List<DietResponseDto.FoodResponseDto> foods = diet.getFoods().stream()
                .map(dietFood -> DietResponseDto.FoodResponseDto.builder()
                        .foodId(dietFood.getFood().getId())
                        .name(dietFood.getFood().getName())
                        .calorie(dietFood.getFood().getCalory())
                        .quantity(dietFood.getQuantity())
                        .build())
                .toList();

        List<DietResponseDto.NutrientResponseDto> nutrients = diet.getNutrients().stream()
                .map(dietNutrient -> DietResponseDto.NutrientResponseDto.builder()
                        .name(dietNutrient.getNutrientName())
                        .intake(dietNutrient.getAmount())
                        .unit(dietNutrient.getUnit())
                        .build())
                .toList();

        return DietResponseDto.builder()
                .id(diet.getId())
                .date(diet.getDate())
                .type(diet.getType())
                .satisfaction(diet.getSatisfaction())
                .time(diet.getTime())
                .memo(diet.getMemo())
                .totalCalorie(diet.getTotalCalorie())
                .foods(foods)
                .nutrients(nutrients)
                .build();
    }
}
