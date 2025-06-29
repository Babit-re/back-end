package com.babit.demo.domain.diet.dto;

import com.babit.demo.domain.diet.type.StatusType;
import lombok.*;

import java.util.List;

@Getter
@Builder
public class DietAnalysisResponseDto {

    private double averageCalorie;
    private MacroRatioDto macroRatio;
    private List<NutrientEvaluationDto> nutrientEvaluations;

    @Getter
    @Builder
    public static class MacroRatioDto {
        private double carbohydrate;
        private double protein;
        private double fat;
    }

    @Getter
    @Builder
    public static class NutrientEvaluationDto {
        private String name;
        private double intake;
        private String unit;
        private NutrientRecommendedDto recommended;
        private StatusType status;
    }

    @Getter
    @Builder
    public static class NutrientRecommendedDto {
        private double min;
        private double max;
    }

}
