package com.mrs.enpoint.feature.plan.service;

import com.mrs.enpoint.feature.plan.dto.PlanRequestDTO;
import com.mrs.enpoint.feature.plan.dto.PlanResponseDTO;

import java.util.List;

public interface PlanService {

    PlanResponseDTO createPlan(PlanRequestDTO request);

    List<PlanResponseDTO> getAllPlans();

    PlanResponseDTO getPlanById(int id);

    List<PlanResponseDTO> getPlansByOperator(int operatorId);

    List<PlanResponseDTO> getPlansByCategory(int categoryId);

    PlanResponseDTO updatePlan(int id, PlanRequestDTO request);

    void activatePlan(int id);

    void deactivatePlan(int id);
}