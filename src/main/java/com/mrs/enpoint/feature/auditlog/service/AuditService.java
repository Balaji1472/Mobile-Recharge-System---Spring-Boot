package com.mrs.enpoint.feature.auditlog.service;

//import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
	
//	List<AuditResponseDTO> getAllLogs();
	public Page<AuditResponseDTO> getAllLogs(Pageable pageable);
	
	AuditResponseDTO getLogById(int id);
}
