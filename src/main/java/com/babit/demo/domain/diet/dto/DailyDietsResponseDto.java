package com.babit.demo.domain.diet.dto;

import com.babit.demo.domain.diet.type.DietType;
import lombok.*;

import java.time.*;
import java.util.List;

@Getter
@Builder
public class DailyDietsResponseDto {

    private LocalDate date;
    private List<DietSummaryDto> diets;

    @Getter
    @Builder
    public static class DietSummaryDto {
        private Long id;
        private DietType type;
        private LocalTime time;
        private List<FoodSummaryDto> foods;
    }

    @Getter
    @Builder
    public static class FoodSummaryDto {
        private String name;
    }

}
