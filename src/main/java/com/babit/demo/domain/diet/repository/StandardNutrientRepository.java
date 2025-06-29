package com.babit.demo.domain.diet.repository;

import com.babit.demo.domain.diet.entity.StandardNutrient;
import com.babit.demo.domain.user.type.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface StandardNutrientRepository extends JpaRepository<StandardNutrient, Long> {

    // 성별에 해당하는 모든 영양소 기준 조회
    List<StandardNutrient> findByGender(Gender gender);

}
