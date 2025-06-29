package com.babit.demo.domain.diet.repository;

import com.babit.demo.domain.diet.entity.Diet;
import com.babit.demo.domain.diet.entity.DietFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DietFoodRepository extends JpaRepository<DietFood, Long> {

    // 특정 식단에 연결된 모든 음식 조회
    List<DietFood> findByDiet(Diet diet);

}
