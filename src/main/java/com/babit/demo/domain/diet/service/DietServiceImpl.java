package com.babit.demo.domain.diet.service;

import com.babit.demo.domain.diet.converter.DietConverter;
import com.babit.demo.domain.diet.dto.*;
import com.babit.demo.domain.diet.entity.Diet;
import com.babit.demo.domain.diet.entity.DietFood;
import com.babit.demo.domain.diet.entity.DietNutrient;
import com.babit.demo.domain.diet.repository.DietFoodRepository;
import com.babit.demo.domain.diet.repository.DietNutrientRepository;
import com.babit.demo.domain.diet.repository.DietRepository;
import com.babit.demo.domain.diet.dto.DailyDietsResponseDto;
import com.babit.demo.domain.food.entity.Food;
import com.babit.demo.domain.food.repository.FoodRepository;

import com.babit.demo.domain.user.entity.User;
import com.babit.demo.domain.user.repository.UserRepository;
import com.babit.demo.global.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DietServiceImpl implements DietService {

    private final DietRepository dietRepository;
    private final DietFoodRepository dietFoodRepository;
    private final DietNutrientRepository dietNutrientRepository;
    private final FoodRepository foodRepository;
    private final UserRepository userRepository;

    @Override
    public DailyDietsResponseDto getDailyDiets(Long userId, LocalDate date) {
        List<Diet> diets = dietRepository.findByUserIdAndDate(userId, date);

        List<DailyDietsResponseDto.DietSummaryDto> dietSummaries = diets.stream()
                .map(dietDto -> DailyDietsResponseDto.DietSummaryDto.builder()
                        .id(dietDto.getId())
                        .type(dietDto.getType())
                        .time(dietDto.getTime())
                        .foods(dietDto.getFoods().stream()
                                .map(dietFood -> DailyDietsResponseDto.FoodSummaryDto.builder()
                                        .name(dietFood.getFood().getName())
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());

        return DailyDietsResponseDto.builder()
                .date(date)
                .diets(dietSummaries)
                .build();
    }

    @Override
    public DietResponseDto getDietDetail(Long userId, Long dietId, LocalDate date) {
        Diet diet = dietRepository.findByUserIdAndIdAndDate(userId, dietId, date)
                .orElseThrow(() -> new ResourceNotFoundException("해당 식단을 찾을 수 없습니다."));

        return DietConverter.toResponseDto(diet);
    }

    @Override
    public DietResponseDto createDiet(Long userId, DietRequestDto requestDto, LocalDate date) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));

        // 총 칼로리 계산
        Map<Long, Food> foodMap = new HashMap<>();
        for (DietRequestDto.FoodRequestDto foodDto : requestDto.getFoods()) {
            Long foodId = foodDto.getFoodId();
            Food food = foodRepository.findById(foodId)
                    .orElseThrow(() -> new ResourceNotFoundException("음식을 찾을 수 없습니다."));
            foodMap.put(foodId, food);
        }
        double totalCalorie = DietCalculator.calculateTotalCalorie(requestDto.getFoods(), foodMap);

        // Diet 저장
        Diet diet = Diet.builder()
                .user(user)
                .date(date)
                .time(requestDto.getTime())
                .type(requestDto.getType())
                .satisfaction(requestDto.getSatisfaction())
                .memo(requestDto.getMemo())
                .totalCalorie(totalCalorie)
                .build();
        dietRepository.save(diet);

        // DietFood 저장
        List<DietFood> dietFoods = requestDto.getFoods().stream()
                .map(foodDto -> DietFood.builder()
                        .diet(diet)
                        .food(foodMap.get(foodDto.getFoodId()))
                        .quantity(foodDto.getQuantity())
                        .build())
                .toList();
        dietFoodRepository.saveAll(dietFoods);

        // DietNutrient 계산 후 저장
        List<DietNutrient> dietNutrients = DietCalculator.calculateNutrients(diet);
        dietNutrientRepository.saveAll(dietNutrients);

        return DietConverter.toResponseDto(diet);
    }

    @Override
    public DietResponseDto updateDiet(Long userId, Long dietId, LocalDate date, DietRequestDto requestDto) {
        Diet diet = dietRepository.findByUserIdAndIdAndDate(userId, dietId, date)
                .orElseThrow(() -> new ResourceNotFoundException("해당 식단을 찾을 수 없습니다."));

        // 새로운 총 칼로리 계산
        Map<Long, Food> foodMap = new HashMap<>();
        for (DietRequestDto.FoodRequestDto foodDto : requestDto.getFoods()) {
            Long foodId = foodDto.getFoodId();
            Food food = foodRepository.findById(foodId)
                    .orElseThrow(() -> new ResourceNotFoundException("음식을 찾을 수 없습니다."));
            foodMap.put(foodId, food);
        }
        double totalCalorie = DietCalculator.calculateTotalCalorie(requestDto.getFoods(), foodMap);

        // Diet 업데이트
        diet.updateInfo(
                requestDto.getType(),
                requestDto.getTime(),
                requestDto.getSatisfaction(),
                requestDto.getMemo(),
                totalCalorie
        );
        diet.getFoods().clear();
        diet.getNutrients().clear();

        // DietFood 저장
        List<DietFood> dietFoods = requestDto.getFoods().stream()
                .map(foodDto -> DietFood.builder()
                        .diet(diet)
                        .food(foodMap.get(foodDto.getFoodId()))
                        .quantity(foodDto.getQuantity())
                        .build())
                .toList();
        dietFoodRepository.saveAll(dietFoods);
        diet.updateFoods(dietFoods);

        // DietNutrient 계산 후 저장
        List<DietNutrient> dietNutrients = DietCalculator.calculateNutrients(diet);
        dietNutrientRepository.saveAll(dietNutrients);
        diet.updateNutrients(dietNutrients);

        return DietConverter.toResponseDto(diet);
    }

    @Override
    public void deleteDiet(Long userId, Long dietId, LocalDate date) {
        Diet diet = dietRepository.findByUserIdAndIdAndDate(userId, dietId, date)
                .orElseThrow(() -> new ResourceNotFoundException("해당 식단을 찾을 수 없습니다."));

        dietRepository.delete(diet);
    }
}
