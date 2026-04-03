package com.mrs.enpoint.entity;

import java.util.List;

import com.mrs.enpoint.feature.auth.enums.Status;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "operator")
public class Operator {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int operatorId;

	@NotBlank
	@Column(name = "operator_name", nullable = false, unique = true)
	private String operatorName;
 
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private Status status = Status.ACTIVE;
	
	@OneToMany(mappedBy = "operator", fetch = FetchType.LAZY)
    private List<Plan> plans;

	public Operator() {
	}

	public Operator(String operatorName, Status status) {
		this.operatorName = operatorName;
		this.status = status;
	}

	public int getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(int operatorId) {
		this.operatorId = operatorId;
	}

	public String getName() {
		return operatorName;
	}

	public void setName(String operatorName) {
		this.operatorName = operatorName;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public List<Plan> getPlans() {
		return plans;
	}

	public void setPlans(List<Plan> plans) {
		this.plans = plans;
	}
	
	
}
