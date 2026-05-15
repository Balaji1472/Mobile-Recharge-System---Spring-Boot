package com.mrs.enpoint.feature.recharge.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mrs.enpoint.feature.plan.dto.PlanResponseDTO;
import com.mrs.enpoint.feature.recharge.dto.QuickFormRechargeRequestDTO;
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

    @GetMapping("/connection")
    public ResponseEntity<Integer> getConnectionId(@RequestParam String mobileNumber) {
        return ResponseEntity.ok(rechargeService.getConnectionIdFromMobile(mobileNumber));
    }

    @GetMapping("/lookup/{mobileNumber}")
    public ResponseEntity<List<PlanResponseDTO>> getPlansForMobile(
            @PathVariable String mobileNumber) {
        return ResponseEntity.ok(rechargeService.getPlansForMobileNumber(mobileNumber));
    }
    
    @PostMapping("/validate-quick-recharge")
    public ResponseEntity<String> validateQuickRecharge(@Valid @RequestBody QuickFormRechargeRequestDTO request) {
        rechargeService.validateQuickRecharge(request);
        
        return ResponseEntity.ok("Validation successful. Proceed to plans");
    }

    @PostMapping
    public ResponseEntity<RechargeResponseDTO> initiateRecharge(
            @Valid @RequestBody RechargeRequestDTO request) {
        return ResponseEntity.ok(rechargeService.initiateRecharge(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RechargeResponseDTO> getRechargeById(@PathVariable int id) {
        return ResponseEntity.ok(rechargeService.getRechargeById(id));
    }

    @GetMapping("/my")
    public ResponseEntity<List<RechargeResponseDTO>> getMyRecharges() {
        return ResponseEntity.ok(rechargeService.getMyRecharges());
    }
}