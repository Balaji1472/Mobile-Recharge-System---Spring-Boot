package com.mrs.enpoint.feature.recharge.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mrs.enpoint.feature.plan.dto.PlanResponseDTO;
import com.mrs.enpoint.feature.recharge.dto.RechargeRequestDTO;
import com.mrs.enpoint.feature.recharge.dto.RechargeResponseDTO;
import com.mrs.enpoint.feature.recharge.service.RechargeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/recharges")
public class RechargeController {

	private final RechargeService rechargeService;

	public RechargeController(RechargeService rechargeService) {
		this.rechargeService = rechargeService;
	}

	// post
	@PostMapping
	public ResponseEntity<RechargeResponseDTO> initiateRecharge(@Valid @RequestBody RechargeRequestDTO request) {
		return new ResponseEntity<>(rechargeService.initiateRecharge(request), HttpStatus.CREATED);
	}
	
	//get
	@GetMapping("/lookup/{mobileNumber}")
	public ResponseEntity<List<PlanResponseDTO>> getAvailablePlans(@PathVariable String mobileNumber) {
	    return ResponseEntity.ok(rechargeService.getPlansForMobileNumber(mobileNumber));
	}
	
	// get users recharge
	@GetMapping("/my")
    public ResponseEntity<List<RechargeResponseDTO>> getMyRecharges() {
        return ResponseEntity.ok(rechargeService.getMyRecharges());
    }
	
	// get recharge by id
	@GetMapping("/{id}")
    public ResponseEntity<RechargeResponseDTO> getRechargeById(@PathVariable int id) {
        return ResponseEntity.ok(rechargeService.getRechargeById(id));
    }
	
}
