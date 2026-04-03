package com.mrs.enpoint.feature.auditlog.mapper;

import com.mrs.enpoint.entity.AuditLog;
import com.mrs.enpoint.feature.auditlog.dto.AuditResponseDTO;

public class AuditMapper {

	private AuditMapper() {
		
	}
	
	public static AuditResponseDTO toResponseDTO(AuditLog log) {	
		
		AuditResponseDTO dto = new AuditResponseDTO();
		
		dto.setEntityName(log.getEntityName());
		dto.setEntityId(log.getEntityId());
		dto.setAction(log.getAction());
		dto.setPerformedBy(log.getPerformedBy());
		dto.setOldValue(log.getOldValue());
		dto.setNewValue(log.getNewValue());
		dto.setTimestamp(log.getTimestamp());
		
		return dto;
	}

}
