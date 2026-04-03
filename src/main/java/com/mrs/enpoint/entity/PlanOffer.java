package com.mrs.enpoint.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "plan_offer")
public class PlanOffer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "plan_offer_id")
	private int planOfferId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "plan_id", nullable = false)
	private Plan plan;
 
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "offer_id", nullable = false)
	private Offer offer;

	@Column(name = "priority")
	private int priority;

	public int getPlanOfferId() {
		return planOfferId;
	}

	public void setPlanOfferId(int planOfferId) {
		this.planOfferId = planOfferId;
	}

	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	public Offer getOffer() {
		return offer;
	}

	public void setOffer(Offer offer) {
		this.offer = offer;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}
}
