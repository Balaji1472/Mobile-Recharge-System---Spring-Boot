package com.mrs.enpoint.feature.recharge.service;

import java.util.List;

import com.mrs.enpoint.feature.recharge.dto.RechargeRequestDTO;
import com.mrs.enpoint.feature.recharge.dto.RechargeResponseDTO;

public interface RechargeService {

    // POST /recharges — logged-in user initiates a recharge
	RechargeResponseDTO initiateRecharge(RechargeRequestDTO request);
	
	// GET /recharges/{id} — get any recharge by id (user sees only their own, admin sees all)
    RechargeResponseDTO getRechargeById(int rechargeId);
    
    // GET /recharges/my — all recharges initiated by the logged-in user
    List<RechargeResponseDTO> getMyRecharges();
}
