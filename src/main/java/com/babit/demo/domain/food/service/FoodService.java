package com.babit.demo.domain.food.service;

import com.babit.demo.domain.food.dto.FoodSearchResponseDto;

import java.util.List;
import java.util.Map;

public interface FoodService {

    //식단 등록 시 음식 검색
    public List<FoodSearchResponseDto> searchFoodsByKeyword(String keyword);

}
