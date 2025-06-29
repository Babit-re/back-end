package com.babit.demo.domain.diet.repository;

import com.babit.demo.domain.diet.entity.Diet;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;

@Repository
public interface DietRepository extends JpaRepository<Diet, Long> {

    // 특정 날짜의 모든 식단 조회
    List<Diet> findByUserIdAndDate(Long userId, LocalDate date);

    // 날짜와 dietId로 단일 식단 조회
    Optional<Diet> findByUserIdAndIdAndDate(Long userId, Long dietId, LocalDate date);

    // 특정 기간의 모든 식단 조회
    @EntityGraph(attributePaths = "nutrients")
    List<Diet> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
}
