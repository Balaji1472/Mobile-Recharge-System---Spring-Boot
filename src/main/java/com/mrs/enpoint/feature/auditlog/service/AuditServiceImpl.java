package com.mrs.enpoint.feature.auditlog.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.mrs.enpoint.entity.AuditLog;
import com.mrs.enpoint.feature.auditlog.dto.AuditResponseDTO;
import com.mrs.enpoint.feature.auditlog.enums.AuditAction;
import com.mrs.enpoint.feature.auditlog.enums.EntityName;
import com.mrs.enpoint.feature.auditlog.mapper.AuditMapper;
import com.mrs.enpoint.feature.auditlog.repository.AuditLogRepository;
import com.mrs.enpoint.shared.exception.NotFoundException;

@Service
public class AuditServiceImpl implements AuditService {

	private final AuditLogRepository auditLogRepository;

	public AuditServiceImpl(AuditLogRepository auditLogRepository) {
		this.auditLogRepository = auditLogRepository;
	}

	@Override
	public void log(int performedBy, EntityName entityName, int entityId, AuditAction action, String oldValue,
			String newValue) {
		AuditLog log = new AuditLog();

		log.setPerformedBy(performedBy);
		log.setEntityName(entityName);
		log.setEntityId(entityId);
		log.setAction(action);
		log.setOldValue(oldValue);
		log.setNewValue(newValue);
		log.setTimestamp(LocalDateTime.now());

		auditLogRepository.save(log);

	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public List<AuditResponseDTO> getAllLogs() {
		return auditLogRepository.findAll().stream().map(audit -> AuditMapper.toResponseDTO(audit))
				.collect(Collectors.toList());
	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public AuditResponseDTO getLogById(int id) {
		AuditLog auditLog = auditLogRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Log not found with id: "+id));
		return AuditMapper.toResponseDTO(auditLog);
	}
	
}
