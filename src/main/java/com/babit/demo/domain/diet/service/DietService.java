package com.babit.demo.domain.diet.service;

import com.babit.demo.domain.diet.dto.*;

import java.time.LocalDate;

public interface DietService {

    // 특정 날짜의 전체 식단 조회
    DailyDietsResponseDto getDailyDiets(Long userId, LocalDate date);

    // 식단 CRUD
    DietResponseDto getDietDetail(Long userId, Long dietId, LocalDate date);

    DietResponseDto createDiet(Long userId, DietRequestDto requestDto, LocalDate date);

    DietResponseDto updateDiet(Long userId, Long dietId, LocalDate date, DietRequestDto requestDto);

    void deleteDiet(Long userId, Long dietId, LocalDate date);

}
