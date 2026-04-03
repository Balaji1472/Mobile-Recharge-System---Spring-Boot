package com.mrs.enpoint.feature.offer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.mrs.enpoint.entity.Offer;

import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OfferRepository extends JpaRepository<Offer, Integer> {
 
    // Get active & valid offers
    List<Offer> findByIsActiveTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate currentDate1,
			LocalDate currentDate2);

	// Get single active offer
	Optional<Offer> findByOfferIdAndIsActiveTrue(int offerId);

	// Get only active offers
	List<Offer> findByIsActiveTrue();
	
	boolean existsByTitle(String operatorName);

	// Deactivate expired offers
	@Modifying
	@Transactional
	@Query("UPDATE Offer o SET o.isActive = false WHERE o.endDate < CURRENT_DATE AND o.isActive = true")
	int deactivateExpiredOffers();
}
