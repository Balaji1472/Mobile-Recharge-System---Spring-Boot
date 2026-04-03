package com.mrs.enpoint.entity;

import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "category")
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int categoryId;

	@NotBlank
	@Column(name = "category_code", nullable = false, unique = true)
	private String categoryCode;

	@NotBlank
	@Column(name = "display_name", nullable = false)
	private String displayName;

	@Column(name = "is_active")
	private Boolean isActive = true;
	
	@OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Plan> plans;

	public Category() {
	}

	public Category(String categoryCode, String displayName, Boolean isActive) {
		this.categoryCode = categoryCode;
		this.displayName = displayName;
		this.isActive = isActive;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public List<Plan> getPlans() {
		return plans;
	}

	public void setPlans(List<Plan> plans) {
		this.plans = plans;
	}
	
}
