package com.mrs.enpoint.feature.planoffer.mapper;

import com.mrs.enpoint.entity.PlanOffer;
import com.mrs.enpoint.feature.planoffer.dto.PlanOfferResponseDTO;

public class PlanOfferMapper {

    private PlanOfferMapper() {
    }

    public static PlanOfferResponseDTO toResponseDTO(PlanOffer planOffer) {
        PlanOfferResponseDTO dto = new PlanOfferResponseDTO();
        dto.setPlanOfferId(planOffer.getPlanOfferId());
        dto.setPlanId(planOffer.getPlan().getPlanId());
        dto.setPlanName(planOffer.getPlan().getPlanName());
        dto.setOfferId(planOffer.getOffer().getOfferId());
        dto.setOfferTitle(planOffer.getOffer().getTitle());
        dto.setDiscountType(planOffer.getOffer().getDiscountType());
        dto.setDiscountValue(planOffer.getOffer().getDiscountValue());
        dto.setStartDate(planOffer.getOffer().getStartDate());
        dto.setEndDate(planOffer.getOffer().getEndDate());
        dto.setOfferIsActive(planOffer.getOffer().getIsActive());
        dto.setPriority(planOffer.getPriority());
        return dto;
    }
}