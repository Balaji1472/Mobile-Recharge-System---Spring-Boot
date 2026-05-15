package com.mrs.enpoint.feature.plan.repository;

import com.mrs.enpoint.entity.Plan;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Integer> {

    boolean existsByOperator_OperatorIdAndPlanName(int operatorId, String planName);

    List<Plan> findByOperator_OperatorId(int operatorId);
    
    boolean existsByOperator_OperatorIdAndIsActive(int operatorId, boolean isActive);

    List<Plan> findByCategory_CategoryId(int categoryId);

    boolean existsByCategory_CategoryIdAndIsActive(int categoryId, boolean isActive);
}