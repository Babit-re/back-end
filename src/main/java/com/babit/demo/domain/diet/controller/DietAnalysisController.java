package com.babit.demo.domain.diet.controller;

import com.babit.demo.domain.auth.jwt.CustomUserDetails;
import com.babit.demo.domain.diet.dto.DietAnalysisResponseDto;
import com.babit.demo.domain.diet.service.DietAnalysisService;
import com.babit.demo.domain.diet.type.AnalysisPeriodType;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/diets/analysis")
@RequiredArgsConstructor
public class DietAnalysisController {

    private final DietAnalysisService dietAnalysisService;

    @GetMapping("/{period}")
    public ResponseEntity<DietAnalysisResponseDto> getAnalyzeDiet(
            @PathVariable("period") AnalysisPeriodType period,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getId();
        DietAnalysisResponseDto response = dietAnalysisService.analyzeDiet(userId, period, date);
        return ResponseEntity.ok(response);
    }
}

