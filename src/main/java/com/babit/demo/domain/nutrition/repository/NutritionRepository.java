package com.babit.demo.domain.nutrition.repository;

import com.babit.demo.domain.nutrition.entity.Nutrition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NutritionRepository extends JpaRepository<Nutrition, Long> {
}
