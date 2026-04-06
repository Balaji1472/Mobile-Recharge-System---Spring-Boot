package com.mrs.enpoint.feature.savednumber.mapper;

import com.mrs.enpoint.entity.SavedMobileNumber;
import com.mrs.enpoint.feature.savednumber.dto.SavedNumberResponseDTO;

public class SavedNumberMapper {

    private SavedNumberMapper() {}

    public static SavedNumberResponseDTO toResponseDTO(SavedMobileNumber saved) {
        SavedNumberResponseDTO dto = new SavedNumberResponseDTO();
        dto.setId(saved.getId());
        dto.setUserId(saved.getUser().getUserId());
        dto.setMobileNumber(saved.getMobileNumber());
        dto.setOperatorName(saved.getOperator().getOperatorName());
        dto.setNickname(saved.getNickname());
        return dto;
    }
}