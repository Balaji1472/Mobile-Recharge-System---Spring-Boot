package com.mrs.enpoint.feature.plan.repository;

import com.mrs.enpoint.entity.Plan;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Integer> {

    // Check duplicate plan name under same operator
    boolean existsByOperator_OperatorIdAndPlanName(int operatorId, String planName);

    // Get all plans under a specific operator
    List<Plan> findByOperator_OperatorId(int operatorId);
    
    // check presence of plan by operator 
    boolean existsByOperator_OperatorIdAndIsActive(int operatorId, boolean isActive);

    // Get all plans under a specific category
    List<Plan> findByCategory_CategoryId(int categoryId);

    // Used in CategoryServiceImpl to check active plans before deactivating category
    boolean existsByCategory_CategoryIdAndIsActive(int categoryId, boolean isActive);
}