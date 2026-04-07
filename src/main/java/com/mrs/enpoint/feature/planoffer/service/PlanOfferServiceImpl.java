package com.mrs.enpoint.feature.planoffer.service;

import com.mrs.enpoint.entity.Offer;
import com.mrs.enpoint.entity.Plan;
import com.mrs.enpoint.entity.PlanOffer;
import com.mrs.enpoint.feature.auditlog.enums.AuditAction;
import com.mrs.enpoint.feature.auditlog.enums.EntityName;
import com.mrs.enpoint.feature.auditlog.service.AuditService;
import com.mrs.enpoint.feature.offer.repository.OfferRepository;
import com.mrs.enpoint.feature.plan.repository.PlanRepository;
import com.mrs.enpoint.feature.planoffer.dto.PlanOfferRequestDTO;
import com.mrs.enpoint.feature.planoffer.dto.PlanOfferResponseDTO;
import com.mrs.enpoint.feature.planoffer.mapper.PlanOfferMapper;
import com.mrs.enpoint.feature.planoffer.repository.PlanOfferRepository;
import com.mrs.enpoint.shared.exception.DuplicateAlreadyExistsException;
import com.mrs.enpoint.shared.exception.NotFoundException;
import com.mrs.enpoint.shared.security.SecurityUtils;

import jakarta.transaction.Transactional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlanOfferServiceImpl implements PlanOfferService {

	private final PlanOfferRepository planOfferRepository;
	private final PlanRepository planRepository;
	private final OfferRepository offerRepository;
	private final AuditService auditService;
	private final SecurityUtils securityUtils;

	public PlanOfferServiceImpl(PlanOfferRepository planOfferRepository, PlanRepository planRepository,
			OfferRepository offerRepository, AuditService auditService, SecurityUtils securityUtils) {
		this.planOfferRepository = planOfferRepository;
		this.planRepository = planRepository;
		this.offerRepository = offerRepository;
		this.auditService = auditService;
		this.securityUtils = securityUtils;
	}

	@Override
	public List<PlanOfferResponseDTO> getOffersByPlan(int planId) {
		if (!planRepository.existsById(planId)) {
			throw new NotFoundException("Plan not found with id: " + planId);
		}
		
		if(planOfferRepository.findByPlan_PlanId(planId).isEmpty()) {
			throw new NotFoundException("No Offers are available for this planId: "+planId);
		}
		
		return planOfferRepository.findByPlan_PlanId(planId).stream().map(planOffer -> PlanOfferMapper.toResponseDTO(planOffer))
				.collect(Collectors.toList());
	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@Transactional
	public PlanOfferResponseDTO mapOfferToPlan(int planId, PlanOfferRequestDTO request) {
		Plan plan = planRepository.findById(planId)
				.orElseThrow(() -> new NotFoundException("Plan not found with id: " + planId));

		Offer offer = offerRepository.findById(request.getOfferId())
				.orElseThrow(() -> new NotFoundException("Offer not found with id: " + request.getOfferId()));

		if (planOfferRepository.existsByPlan_PlanIdAndOffer_OfferId(planId, request.getOfferId())) {
			throw new DuplicateAlreadyExistsException(
					"Offer with id " + request.getOfferId() + " is already mapped to plan with id " + planId);
		}

		PlanOffer planOffer = new PlanOffer();
		planOffer.setPlan(plan);
		planOffer.setOffer(offer);
		planOffer.setPriority(request.getPriority());

		PlanOffer saved = planOfferRepository.save(planOffer);

		auditService.log(securityUtils.getCurrentUserId(), EntityName.PLAN, planId, AuditAction.MAP_PLAN_OFFER, null,
				"Mapped offerId=" + offer.getOfferId() + " to planId=" + planId);

		return PlanOfferMapper.toResponseDTO(saved);
	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@Transactional
	public void unmapOfferFromPlan(int planId, int offerId) {
		if (!planRepository.existsById(planId)) {
			throw new NotFoundException("Plan not found with id: " + planId);
		}

		PlanOffer planOffer = planOfferRepository.findByPlan_PlanIdAndOffer_OfferId(planId, offerId)
				.orElseThrow(() -> new NotFoundException(
						"No mapping found for planId=" + planId + " and offerId=" + offerId));

		planOfferRepository.delete(planOffer);

		auditService.log(securityUtils.getCurrentUserId(), EntityName.PLAN, planId, AuditAction.UNMAP_PLAN_OFFER,
				"offerId=" + offerId + " mapped to planId=" + planId, null);
	}
}