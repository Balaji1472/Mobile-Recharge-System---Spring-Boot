package com.mrs.enpoint.feature.operator.mapper;

import com.mrs.enpoint.entity.Operator;
import com.mrs.enpoint.feature.operator.dto.OperatorResponseDTO;

public class OperatorMapper {

    public static OperatorResponseDTO toResponseDTO(Operator operator) {
        OperatorResponseDTO dto = new OperatorResponseDTO();
        dto.setOperatorId(operator.getOperatorId());
        dto.setOperatorName(operator.getName());
        dto.setStatus(operator.getStatus());
        return dto;
    }
}