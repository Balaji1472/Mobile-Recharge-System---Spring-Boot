package com.mrs.enpoint.feature.recharge.service;

import java.util.List;

import com.mrs.enpoint.feature.plan.dto.PlanResponseDTO;
import com.mrs.enpoint.feature.recharge.dto.QuickFormRechargeRequestDTO;
import com.mrs.enpoint.feature.recharge.dto.RechargeRequestDTO;
import com.mrs.enpoint.feature.recharge.dto.RechargeResponseDTO;

public interface RechargeService {

    RechargeResponseDTO initiateRecharge(RechargeRequestDTO request);

    List<PlanResponseDTO> getPlansForMobileNumber(String mobileNumber);

    RechargeResponseDTO getRechargeById(int rechargeId);

    List<RechargeResponseDTO> getMyRecharges();

    int getConnectionIdFromMobile(String mobileNumber);
    
    public boolean validateQuickRecharge(QuickFormRechargeRequestDTO request);
}