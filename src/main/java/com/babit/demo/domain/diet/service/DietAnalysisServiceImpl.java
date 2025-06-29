package com.babit.demo.domain.diet.service;

import com.babit.demo.domain.diet.dto.DietAnalysisResponseDto;
import com.babit.demo.domain.diet.entity.*;
import com.babit.demo.domain.diet.repository.*;
import com.babit.demo.domain.diet.type.*;
import com.babit.demo.domain.user.entity.User;
import com.babit.demo.domain.user.repository.UserRepository;
import com.babit.demo.global.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class DietAnalysisServiceImpl implements DietAnalysisService {

    private final DietRepository dietRepository;
    private final DietNutrientRepository dietNutrientRepository;
    private final StandardNutrientRepository standardNutrientRepository;
    private final UserRepository userRepository;

    @Override
    public DietAnalysisResponseDto analyzeDiet(Long userId, AnalysisPeriodType period, LocalDate baseDate) {

        // 기간 설정
        LocalDate startDate, endDate;

        switch (period) {
            case WEEK -> {
                startDate = baseDate.minusDays(6);
                endDate = baseDate;
            }
            case MONTH -> {
                startDate = baseDate.withDayOfMonth(1);
                endDate = baseDate.withDayOfMonth(baseDate.lengthOfMonth());
            }
            default -> {
                startDate = baseDate;
                endDate = baseDate;
            }
        }

        // 기간에 해당하는 식단 가져오기
        List<Diet> diets = dietRepository.findByUserIdAndDateBetween(userId, startDate, endDate);

        if (diets.isEmpty()) {
            return DietAnalysisResponseDto.builder()
                    .averageCalorie(0)
                    .macroRatio(DietAnalysisResponseDto.MacroRatioDto.builder()
                            .carbohydrate(0).protein(0).fat(0).build())
                    .build();
        }

        // 평균 칼로리 계산
        double totalCalories = 0;
        for (Diet diet : diets) totalCalories += diet.getTotalCalorie();
        double averageCalorie = totalCalories / diets.size();

        // 탄단지 비율 계산
        double totalCarb = 0, totalProtein = 0, totalFat = 0;

        for (Diet diet : diets) {
            for (DietNutrient nutrient : diet.getNutrients()) {
                String name = nutrient.getNutrientName();
                double amount = nutrient.getAmount();
                switch (name) {
                    case "탄수화물" -> totalCarb += amount;
                    case "단백질" -> totalProtein += amount;
                    case "지방" -> totalFat += amount;
                }
            }
        }

        double macroSum = totalCarb + totalProtein + totalFat;

        double carbRatio, proteinRatio, fatRatio;

        if (macroSum == 0) {
            carbRatio = 0;
            proteinRatio = 0;
            fatRatio = 0;
        } else {
            carbRatio = (totalCarb / macroSum) * 100;
            proteinRatio = (totalProtein / macroSum) * 100;
            fatRatio = (totalFat / macroSum) * 100;
        }

        DietAnalysisResponseDto.MacroRatioDto macroRatio =
                DietAnalysisResponseDto.MacroRatioDto.builder()
                        .carbohydrate(carbRatio)
                        .protein(proteinRatio)
                        .fat(fatRatio)
                        .build();

        // 권장 섭취량 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));
        List<StandardNutrient> standards = standardNutrientRepository.findByGender(user.getGender());

        // 영양소 섭취량 집계
        Map<String, Double> totalNutrientMap = new HashMap<>();
        for (Diet diet : diets) {
            for (DietNutrient nutrient : diet.getNutrients()) {
                totalNutrientMap.merge(nutrient.getNutrientName(), nutrient.getAmount(), Double::sum);
            }
        }

        List<DietAnalysisResponseDto.NutrientEvaluationDto> evaluations = new ArrayList<>();
        for (StandardNutrient standard : standards) {
            String name = standard.getNutrientName();
            double intake = totalNutrientMap.getOrDefault(name, 0.0);
            StatusType status;
            if (intake < standard.getMinAmount())
                status = StatusType.LOW;
            else if (intake > standard.getMaxAmount())
                status = StatusType.HIGH;
            else
                status = StatusType.NORMAL;

            evaluations.add(DietAnalysisResponseDto.NutrientEvaluationDto.builder()
                    .name(name)
                    .intake(intake)
                    .unit(standard.getUnit())
                    .recommended(DietAnalysisResponseDto.NutrientRecommendedDto.builder()
                            .min(standard.getMinAmount())
                            .max(standard.getMaxAmount())
                            .build())
                    .status(status)
                    .build());
        }

        // 최종 응답 생성
        return DietAnalysisResponseDto.builder()
                .averageCalorie(averageCalorie)
                .macroRatio(macroRatio)
                .nutrientEvaluations(evaluations)
                .build();
    }
}
