package com.mrs.enpoint.feature.plan.controller;

import com.mrs.enpoint.feature.plan.dto.PlanRequestDTO;
import com.mrs.enpoint.feature.plan.dto.PlanResponseDTO;
import com.mrs.enpoint.feature.plan.service.PlanService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plans")
public class PlanController {

	private final PlanService planService;

	public PlanController(PlanService planService) {
		this.planService = planService;
	}

	// Create
	@PostMapping
	public ResponseEntity<PlanResponseDTO> createPlan(@RequestBody PlanRequestDTO request) {
		return new ResponseEntity<>(planService.createPlan(request), HttpStatus.CREATED);
	}

	// Get all
	@GetMapping
	public ResponseEntity<List<PlanResponseDTO>> getAllPlans() {
		return ResponseEntity.ok(planService.getAllPlans());
	}

	// Get by id
	@GetMapping("/{id}")
	public ResponseEntity<PlanResponseDTO> getPlanById(@PathVariable int id) {
		return ResponseEntity.ok(planService.getPlanById(id));
	}

	// Get by operator
	@GetMapping("/operator/{operatorId}")
	public ResponseEntity<List<PlanResponseDTO>> getPlansByOperator(@PathVariable int operatorId) {
		return ResponseEntity.ok(planService.getPlansByOperator(operatorId));
	}

	// Get by category
	@GetMapping("/category/{categoryId}")
	public ResponseEntity<List<PlanResponseDTO>> getPlansByCategory(@PathVariable int categoryId) {
		return ResponseEntity.ok(planService.getPlansByCategory(categoryId));
	}

	// Update
	@PutMapping("/{id}")
	public ResponseEntity<PlanResponseDTO> updatePlan(@PathVariable int id, @RequestBody PlanRequestDTO request) {
		return ResponseEntity.ok(planService.updatePlan(id, request));
	}

	// Activate
	@PutMapping("/{id}/activate")
	public ResponseEntity<String> activatePlan(@PathVariable int id) {
		planService.activatePlan(id);
		return ResponseEntity.ok("Plan activated successfully");
	}

	// Deactivate
	@PutMapping("/{id}/deactivate")
	public ResponseEntity<String> deactivatePlan(@PathVariable int id) {
		planService.deactivatePlan(id);
		return ResponseEntity.ok("Plan deactivated successfully");
	}
}