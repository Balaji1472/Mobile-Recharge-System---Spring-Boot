package com.mrs.enpoint.feature.category.dto;

public class CategoryResponseDTO {

	private int categoryId;
	private String categoryCode;
	private String displayName;
	private long totalPlans;
	private Boolean isActive;

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public long getTotalPlans() {
		return totalPlans;
	}

	public void setTotalPlans(long totalPlans) {
		this.totalPlans = totalPlans;
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
}