package com.babit.demo.domain.diet.service;

import com.babit.demo.domain.diet.dto.DietAnalysisResponseDto;
import com.babit.demo.domain.diet.type.AnalysisPeriodType;

import java.time.LocalDate;

public interface DietAnalysisService {

    // 식단 분석
    DietAnalysisResponseDto analyzeDiet(Long userId, AnalysisPeriodType period, LocalDate baseDate);

}
