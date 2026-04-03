package com.mrs.enpoint.feature.offer.scheduler;

import com.mrs.enpoint.feature.auditlog.enums.AuditAction;
import com.mrs.enpoint.feature.auditlog.enums.EntityName;
import com.mrs.enpoint.feature.auditlog.service.AuditService;
import com.mrs.enpoint.feature.offer.repository.OfferRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OfferScheduler {

	private final OfferRepository offerRepository;
	private final AuditService auditService;

	public OfferScheduler(OfferRepository offerRepository, AuditService auditService) {
		this.offerRepository = offerRepository;
		this.auditService = auditService;
	}

	// runs every hour
	@Scheduled(cron = "0 0 * * * ?")
	public void deactivateExpiredOffers() {
		int updatedCount = offerRepository.deactivateExpiredOffers();

		auditService.log(0, EntityName.OFFER, 0, AuditAction.CRON_JOB_EXECUTION, null,
				"Auto-expired offers deactivated: " + updatedCount);
	}
}