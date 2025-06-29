package com.babit.demo.domain.diet.controller;

import com.babit.demo.domain.auth.jwt.CustomUserDetails;
import com.babit.demo.domain.diet.dto.*;
import com.babit.demo.domain.diet.service.DietService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/diets")
@RequiredArgsConstructor
public class DietController {

    private final DietService dietService;

    @GetMapping("/{date}")
    public ResponseEntity<DailyDietsResponseDto> getDailyDiets(
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getId();
        DailyDietsResponseDto response = dietService.getDailyDiets(userId, date);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{dietId}")
    public ResponseEntity<DietResponseDto> getDietDetail(
            @PathVariable("dietId") Long dietId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getId();
        DietResponseDto response = dietService.getDietDetail(userId, dietId, date);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{date}")
    public ResponseEntity<DietResponseDto> createDiet(
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestBody @Valid DietRequestDto requestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getId();
        DietResponseDto response = dietService.createDiet(userId, requestDto, date);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{dietId}")
    public ResponseEntity<DietResponseDto> updateDiet(
            @PathVariable("dietId") Long dietId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestBody @Valid DietRequestDto requestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getId();
        DietResponseDto response = dietService.updateDiet(userId, dietId, date, requestDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{dietId}")
    public ResponseEntity<Void> deleteDiet(
            @PathVariable("dietId") Long dietId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getId();
        dietService.deleteDiet(userId, dietId, date);
        return ResponseEntity.noContent().build();
    }

}