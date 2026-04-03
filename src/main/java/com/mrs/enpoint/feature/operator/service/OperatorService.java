package com.mrs.enpoint.feature.operator.service;

import java.util.List;

import com.mrs.enpoint.feature.operator.dto.OperatorRequestDTO;
import com.mrs.enpoint.feature.operator.dto.OperatorResponseDTO;

public interface OperatorService {

    List<OperatorResponseDTO> getAllOperators();
    OperatorResponseDTO getOperatorById(int id);
    OperatorResponseDTO createOperator(OperatorRequestDTO request);
    OperatorResponseDTO updateOperator(int id, OperatorRequestDTO request);
    void activateOperator(int id);
    void deactivateOperator(int id);
    		
}