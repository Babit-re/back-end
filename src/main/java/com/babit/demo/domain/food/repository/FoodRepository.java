package com.babit.demo.domain.food.repository;

import com.babit.demo.domain.food.dto.FoodSearchResponseDto;
import com.babit.demo.domain.food.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Long> {

    // 음식 이름에 keyword가 포함된 음식 리스트 반환 (부분 일치, 영어는 대소문자 구분 X)
    List<Food> findByNameContainingIgnoreCase(String keyword);  //Containing: SQL의 LIKE %keyword%와 유사
}