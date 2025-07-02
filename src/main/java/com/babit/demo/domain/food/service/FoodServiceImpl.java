package com.babit.demo.domain.food.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.babit.demo.domain.food.dto.FoodSearchResponseDto;
import com.babit.demo.domain.food.entity.Food;
import com.babit.demo.domain.food.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FoodServiceImpl implements FoodService {

    private final FoodRepository repo;

    //음식검색
    @Override
    public List<FoodSearchResponseDto> searchFoodsByKeyword(String keyword) {
        List<Food> foods = repo.findByNameContainingIgnoreCase(keyword);
        return foods.stream()
                .map(food -> FoodSearchResponseDto.builder()
                        .foodId(food.getId())
                        .name(food.getName())
                        .calorie(food.getCalory())
                        .quantity(100.0) // 기본 100g 기준 등으로 필요 시 고정값 또는 계산값 넣기
                        .build())
                .collect(Collectors.toList());
    }
}
