package com.mrs.enpoint.feature.auditlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mrs.enpoint.entity.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Integer> {
	
}
