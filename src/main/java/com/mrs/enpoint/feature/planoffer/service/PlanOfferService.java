package com.mrs.enpoint.feature.planoffer.service;

import com.mrs.enpoint.feature.planoffer.dto.PlanOfferRequestDTO;
import com.mrs.enpoint.feature.planoffer.dto.PlanOfferResponseDTO;

import java.util.List;

public interface PlanOfferService {

    List<PlanOfferResponseDTO> getOffersByPlan(int planId);

    PlanOfferResponseDTO mapOfferToPlan(int planId, PlanOfferRequestDTO request);

    void unmapOfferFromPlan(int planId, int offerId);
}