package com.babit.demo.domain.diet.dto;

import com.babit.demo.domain.diet.type.*;
import lombok.*;

import java.time.*;
import java.util.List;

@Getter
@Builder
public class DietResponseDto {

    private Long id;
    private LocalDate date;
    private DietType type;
    private SatisfactionType satisfaction;
    private LocalTime time;
    private String memo;
    private double totalCalorie;
    private List<FoodResponseDto> foods;
    private List<NutrientResponseDto> nutrients;

    @Getter
    @Builder
    public static class FoodResponseDto {
        private Long foodId;
        private String name;
        private double calorie;
        private double quantity;
    }

    @Getter
    @Builder
    public static class NutrientResponseDto {
        private String name;
        private double intake;
        private String unit;
    }

}
