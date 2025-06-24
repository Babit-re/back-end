package com.babit.demo.domain.diet.repository;

import com.babit.demo.domain.diet.entity.Diet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DietRepository extends JpaRepository<Diet, Long> {
}
