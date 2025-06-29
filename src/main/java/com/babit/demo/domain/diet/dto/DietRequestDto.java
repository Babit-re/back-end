package com.babit.demo.domain.diet.dto;

import com.babit.demo.domain.diet.type.*;
import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class DietRequestDto {

    private DietType type;
    private SatisfactionType satisfaction;
    private LocalTime time;
    private String memo;
    private List<FoodRequestDto> foods;

    @Getter
    @Setter
    public static class FoodRequestDto {
        private Long foodId;
        private double quantity;
    }

}
