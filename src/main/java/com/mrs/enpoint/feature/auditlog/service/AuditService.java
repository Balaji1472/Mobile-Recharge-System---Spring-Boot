package com.mrs.enpoint.feature.auditlog.service;

import java.util.List;

import com.mrs.enpoint.feature.auditlog.dto.AuditResponseDTO;
import com.mrs.enpoint.feature.auditlog.enums.AuditAction;
import com.mrs.enpoint.feature.auditlog.enums.EntityName;

public interface AuditService {

	void log(
            int performedBy,
            EntityName entityName,
            int entityId,
            AuditAction action,
            String oldValue,
            String newValue
    );
	
	List<AuditResponseDTO> getAllLogs();
	
	AuditResponseDTO getLogById(int id);
}
