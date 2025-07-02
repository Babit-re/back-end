package com.babit.demo.domain.food.controller;

import com.babit.demo.domain.food.dto.FoodSearchResponseDto;
import com.babit.demo.domain.food.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/foods")
public class FoodController {

    private final FoodService foodService;

    //음식 검색
    @GetMapping("/search")
    public ResponseEntity<List<FoodSearchResponseDto>> searchFoodsByKeyword(@RequestParam String keyword) {
        List<FoodSearchResponseDto> foods = foodService.searchFoodsByKeyword(keyword);
        return ResponseEntity.ok(foods);
    }

}
