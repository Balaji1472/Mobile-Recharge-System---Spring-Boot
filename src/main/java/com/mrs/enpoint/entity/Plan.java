package com.mrs.enpoint.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "recharge_plan")
public class Plan {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "plan_id")
	private int planId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "operator_id", nullable = false)
	private Operator operator;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;

	@Column(name = "plan_name", nullable = false, length = 100)
	private String planName;

	@Column(name = "price", nullable = false)
	private BigDecimal price;

	@Column(name = "validity_days")
	private int validityDays;

	@Column(name = "data_benefits", length = 50)
	private String dataBenefits;

	@Column(name = "call_benefits", length = 50)
	private String callBenefits;

	@Column(name = "sms_benefits", length = 50)
	private String smsBenefits;

	@Column(name = "is_active")
	private Boolean isActive = true;

	public int getPlanId() {
		return planId;
	}

	public void setPlanId(int planId) {
		this.planId = planId;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getValidityDays() {
		return validityDays;
	}

	public void setValidityDays(Integer validityDays) {
		this.validityDays = validityDays;
	}

	public String getDataBenefits() {
		return dataBenefits;
	}

	public void setDataBenefits(String dataBenefits) {
		this.dataBenefits = dataBenefits;
	}

	public String getCallBenefits() {
		return callBenefits;
	}

	public void setCallBenefits(String callBenefits) {
		this.callBenefits = callBenefits;
	}

	public String getSmsBenefits() {
		return smsBenefits;
	}

	public void setSmsBenefits(String smsBenefits) {
		this.smsBenefits = smsBenefits;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
}