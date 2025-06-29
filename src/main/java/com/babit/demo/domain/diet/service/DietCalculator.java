package com.babit.demo.domain.diet.service;

import com.babit.demo.domain.diet.dto.DietRequestDto;
import com.babit.demo.domain.diet.entity.Diet;
import com.babit.demo.domain.diet.entity.DietFood;
import com.babit.demo.domain.diet.entity.DietNutrient;
import com.babit.demo.domain.food.entity.Food;

import java.util.*;

public class DietCalculator {

    // 총 칼로리 계산
    public static double calculateTotalCalorie(
            List<DietRequestDto.FoodRequestDto> foodDtos,
            Map<Long, Food> foodMap
    ) {
        double total = 0;

        for (DietRequestDto.FoodRequestDto dto : foodDtos) {
            Food food = foodMap.get(dto.getFoodId());
            if (food == null) {
                throw new IllegalArgumentException("ID: " + dto.getFoodId() + "에 해당하는 음식이 존재하지 않습니다.");
            }

            total += dto.getQuantity() * food.getCalory();
        }

        return total;
    }


    // 총 영양소 계산
    public static List<DietNutrient> calculateNutrients(Diet diet) {
        double totalCarbs = 0;
        double totalProtein = 0;
        double totalFat = 0;
        double totalSodium = 0;
        double totalSugar = 0;
        double totalFiber = 0;
        double totalSaturatedFat = 0;

        for (DietFood dietFood : diet.getFoods()) {
            Food food = dietFood.getFood();
            double quantity = dietFood.getQuantity();

            totalCarbs += food.getCarbs() * quantity;
            totalProtein += food.getProtein() * quantity;
            totalFat += food.getFat() * quantity;
            totalSodium += food.getSodium() * quantity;
            totalSugar += food.getSugar() * quantity;
            totalFiber += food.getFiber() * quantity;
            totalSaturatedFat += food.getSaturatedFat() * quantity;
        }

        return List.of(
                DietNutrient.builder().diet(diet).nutrientName("탄수화물").amount(totalCarbs).unit("g").build(),
                DietNutrient.builder().diet(diet).nutrientName("단백질").amount(totalProtein).unit("g").build(),
                DietNutrient.builder().diet(diet).nutrientName("지방").amount(totalFat).unit("g").build(),
                DietNutrient.builder().diet(diet).nutrientName("나트륨").amount(totalSodium).unit("mg").build(),
                DietNutrient.builder().diet(diet).nutrientName("당류").amount(totalSugar).unit("g").build(),
                DietNutrient.builder().diet(diet).nutrientName("식이섬유").amount(totalFiber).unit("g").build(),
                DietNutrient.builder().diet(diet).nutrientName("포화지방").amount(totalSaturatedFat).unit("g").build()
        );
    }
}
