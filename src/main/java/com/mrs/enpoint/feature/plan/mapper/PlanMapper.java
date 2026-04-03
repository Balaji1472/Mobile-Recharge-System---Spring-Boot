package com.mrs.enpoint.feature.plan.mapper;

import com.mrs.enpoint.entity.Plan;
import com.mrs.enpoint.feature.plan.dto.PlanResponseDTO;

public class PlanMapper {

    private PlanMapper() {
    }

    public static PlanResponseDTO toResponseDTO(Plan plan) {
        PlanResponseDTO dto = new PlanResponseDTO();
        dto.setPlanId(plan.getPlanId());
        dto.setOperatorId(plan.getOperator().getOperatorId());
        dto.setOperatorName(plan.getOperator().getOperatorName());
        dto.setCategoryId(plan.getCategory().getCategoryId());
        dto.setCategoryName(plan.getCategory().getDisplayName());
        dto.setPlanName(plan.getPlanName());
        dto.setPrice(plan.getPrice());
        dto.setValidityDays(plan.getValidityDays());
        dto.setDataBenefits(plan.getDataBenefits());
        dto.setCallBenefits(plan.getCallBenefits());
        dto.setSmsBenefits(plan.getSmsBenefits());
        dto.setIsActive(plan.getIsActive());
        return dto;
    }
}