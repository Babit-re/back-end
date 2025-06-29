package com.babit.demo.domain.diet.repository;

import com.babit.demo.domain.diet.entity.Diet;
import com.babit.demo.domain.diet.entity.DietNutrient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DietNutrientRepository extends JpaRepository<DietNutrient, Long> {

    // 특정 식단의 모든 영양소 정보 조회
    List<DietNutrient> findByDiet(Diet diet);

}
