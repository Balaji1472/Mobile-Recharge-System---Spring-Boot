package com.mrs.enpoint.entity;
 
import java.time.LocalDateTime;

import com.mrs.enpoint.feature.auditlog.enums.AuditAction;
import com.mrs.enpoint.feature.auditlog.enums.EntityName;

import jakarta.persistence.*;

@Entity
@Table(name = "audit_log")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int auditId;

    @Column(name="performed_by")
    private int performedBy;

    @Enumerated(EnumType.STRING)
    @Column(name="entity_name")
    private EntityName entityName;

    @Column(name="entity_id")
    private int entityId;

    @Column(name="action")
    @Enumerated(EnumType.STRING)
    private AuditAction action;

    @Column(columnDefinition = "TEXT", name="old_value")
    private String oldValue;

    @Column(columnDefinition = "TEXT", name="new_value")
    private String newValue;

    @Column(name="timestamp")
    private LocalDateTime timestamp;
    
    public AuditLog() {
    	
    }

	public AuditLog(int performedBy, EntityName entityName, int entityId, AuditAction action, String oldValue,
			String newValue, LocalDateTime timestamp) {
		super();
		this.performedBy = performedBy;
		this.entityName = entityName;
		this.entityId = entityId;
		this.action = action;
		this.oldValue = oldValue;
		this.newValue = newValue;
		this.timestamp = timestamp;
	}

	public int getAuditId() {
		return auditId;
	}

	public void setAuditId(int auditId) {
		this.auditId = auditId;
	}

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
