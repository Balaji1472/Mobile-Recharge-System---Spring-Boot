package com.mrs.enpoint.feature.planoffer.repository;

import com.mrs.enpoint.entity.PlanOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlanOfferRepository extends JpaRepository<PlanOffer, Integer> {

    List<PlanOffer> findByPlan_PlanId(int planId);

    Optional<PlanOffer> findByPlan_PlanIdAndOffer_OfferId(int planId, int offerId);

    boolean existsByPlan_PlanIdAndOffer_OfferId(int planId, int offerId);
} 