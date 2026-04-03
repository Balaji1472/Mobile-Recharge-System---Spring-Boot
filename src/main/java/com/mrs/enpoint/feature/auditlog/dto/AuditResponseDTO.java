package com.mrs.enpoint.feature.auditlog.dto;

import java.time.LocalDateTime;

import com.mrs.enpoint.feature.auditlog.enums.AuditAction;
import com.mrs.enpoint.feature.auditlog.enums.EntityName;

public class AuditResponseDTO {

	private int performedBy;

	private EntityName entityName;

	private int entityId;

	private AuditAction action;

	private String oldValue;

	private String newValue;

	private LocalDateTime timestamp;

	public int getPerformedBy() {
		return performedBy;
	}

	public void setPerformedBy(int performedBy) {
		this.performedBy = performedBy;
	}

	public EntityName getEntityName() {
		return entityName;
	}

	public void setEntityName(EntityName entityName) {
		this.entityName = entityName;
	}

	public int getEntityId() {
		return entityId;
	}

	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}

	public AuditAction getAction() {
		return action;
	}

	public void setAction(AuditAction action) {
		this.action = action;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	
}
