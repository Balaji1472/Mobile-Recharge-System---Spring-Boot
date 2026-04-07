package com.mrs.enpoint.feature.analytics.dto;

import java.math.BigDecimal;

public class AdminAnalyticsDTO {

	private long totalRecharges; 
	private long successfulRecharges; 
	private long failedRecharges; 
	private long refundedRecharges; 
	private BigDecimal totalRevenue; 
	private long totalUsers; 
	private long activePlans; 
	private long totalRefundsProcessed; 
	private BigDecimal totalRefundAmount; 

	public long getTotalRecharges() {
		return totalRecharges;
	}

	public void setTotalRecharges(long totalRecharges) {
		this.totalRecharges = totalRecharges;
	}

	public long getSuccessfulRecharges() {
		return successfulRecharges;
	}

	public void setSuccessfulRecharges(long successfulRecharges) {
		this.successfulRecharges = successfulRecharges;
	}

	public long getFailedRecharges() {
		return failedRecharges;
	}

	public void setFailedRecharges(long failedRecharges) {
		this.failedRecharges = failedRecharges;
	}

	public long getRefundedRecharges() {
		return refundedRecharges;
	}

	public void setRefundedRecharges(long refundedRecharges) {
		this.refundedRecharges = refundedRecharges;
	}

	public BigDecimal getTotalRevenue() {
		return totalRevenue;
	}

	public void setTotalRevenue(BigDecimal totalRevenue) {
		this.totalRevenue = totalRevenue;
	}

	public long getTotalUsers() {
		return totalUsers;
	}

	public void setTotalUsers(long totalUsers) {
		this.totalUsers = totalUsers;
	}

	public long getActivePlans() {
		return activePlans;
	}

	public void setActivePlans(long activePlans) {
		this.activePlans = activePlans;
	}

	public long getTotalRefundsProcessed() {
		return totalRefundsProcessed;
	}

	public void setTotalRefundsProcessed(long totalRefundsProcessed) {
		this.totalRefundsProcessed = totalRefundsProcessed;
	}

	public BigDecimal getTotalRefundAmount() {
		return totalRefundAmount;
	}

	public void setTotalRefundAmount(BigDecimal totalRefundAmount) {
		this.totalRefundAmount = totalRefundAmount;
	}
}