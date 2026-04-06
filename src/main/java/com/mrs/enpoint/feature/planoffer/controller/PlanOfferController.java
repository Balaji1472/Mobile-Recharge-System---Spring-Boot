package com.mrs.enpoint.feature.planoffer.controller;

import com.mrs.enpoint.feature.planoffer.dto.PlanOfferRequestDTO;
import com.mrs.enpoint.feature.planoffer.dto.PlanOfferResponseDTO;
import com.mrs.enpoint.feature.planoffer.service.PlanOfferService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plans")
public class PlanOfferController {
 
	private final PlanOfferService planOfferService;

	public PlanOfferController(PlanOfferService planOfferService) {
		this.planOfferService = planOfferService;
	}

	// Public/User — Get all offers mapped to a plan
	@GetMapping("/{planId}/offers")
	public ResponseEntity<List<PlanOfferResponseDTO>> getOffersByPlan(@PathVariable int planId) {
		return ResponseEntity.ok(planOfferService.getOffersByPlan(planId));
	}

	// Admin — Map an offer to a plan
	@PostMapping("/{planId}/offers")
	public ResponseEntity<PlanOfferResponseDTO> mapOfferToPlan(@Valid @PathVariable int planId,
			@RequestBody PlanOfferRequestDTO request) {
		return new ResponseEntity<>(planOfferService.mapOfferToPlan(planId, request), HttpStatus.CREATED);
	}

	// Admin — Remove a mapped offer from a plan
	@DeleteMapping("/{planId}/offers/{offerId}")
	public ResponseEntity<String> unmapOfferFromPlan(@PathVariable int planId, @PathVariable int offerId) {
		planOfferService.unmapOfferFromPlan(planId, offerId);
		return ResponseEntity.ok("Offer unmapped from plan successfully");
	}
}