package com.mrs.enpoint.feature.offer.service;

import com.mrs.enpoint.entity.Offer;
import com.mrs.enpoint.feature.auditlog.enums.AuditAction;
import com.mrs.enpoint.feature.auditlog.enums.EntityName;
import com.mrs.enpoint.feature.auditlog.service.AuditService;
import com.mrs.enpoint.feature.offer.dto.OfferRequestDTO;
import com.mrs.enpoint.feature.offer.dto.OfferResponseDTO;
import com.mrs.enpoint.feature.offer.exception.OfferAlreadyExistsException;
import com.mrs.enpoint.feature.offer.exception.OfferDoesNotExistException;
import com.mrs.enpoint.feature.offer.mapper.OfferMapper;
import com.mrs.enpoint.feature.offer.repository.OfferRepository;
import com.mrs.enpoint.shared.exception.*;
import com.mrs.enpoint.shared.security.SecurityUtils;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OfferServiceImpl implements OfferService {

	private final OfferRepository offerRepository;
	private final AuditService auditService;
	private final SecurityUtils securityUtils;

	public OfferServiceImpl(OfferRepository offerRepository, AuditService auditService, SecurityUtils securityUtils) {
		this.offerRepository = offerRepository;
		this.auditService = auditService;
		this.securityUtils = securityUtils;
	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public OfferResponseDTO createOffer(OfferRequestDTO request) {

		// validation
		if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
			throw new BusinessException("Title is required");
		}

		if (request.getDiscountValue() == null || request.getDiscountValue().compareTo(BigDecimal.ZERO) <= 0) {
			throw new BusinessException("Discount must be greater than zero");
		}

		if (request.getStartDate() == null || request.getEndDate() == null) {
			throw new BusinessException("Start date and end date are required");
		}

		if (request.getStartDate().isAfter(request.getEndDate())) {
			throw new BusinessException("Start date cannot be after end date");
		}

		String offerTitle = request.getTitle().toUpperCase().trim();

		if (offerRepository.existsByTitle(offerTitle)) {
			throw new OfferAlreadyExistsException("Offer already exists");
		}

		Offer offer = new Offer();
		offer.setTitle(request.getTitle().trim());
		offer.setDiscountType(request.getDiscountType());
		offer.setDiscountValue(request.getDiscountValue());
		offer.setStartDate(request.getStartDate());
		offer.setEndDate(request.getEndDate());
		offer.setIsActive(true); // default

		// save
		Offer saved = offerRepository.save(offer);

		auditService.log(securityUtils.getCurrentUserId(), EntityName.OFFER, saved.getOfferId(),
				AuditAction.CREATE_OFFER, null, "Created: " + saved.getTitle());

		return OfferMapper.toResponseDTO(saved);
	}

	@Override
	public List<OfferResponseDTO> getAllOffers() {
		return offerRepository.findAll().stream().map(offer -> OfferMapper.toResponseDTO(offer))
				.collect(Collectors.toList());
	}

	@Override
	public OfferResponseDTO getOfferById(int id) {
		Offer offer = offerRepository.findById(id)
				.orElseThrow(() -> new OfferDoesNotExistException("Offer not found with id: " + id));
		return OfferMapper.toResponseDTO(offer);
	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public OfferResponseDTO updateOffer(int id, OfferRequestDTO request) {
		Offer existing = offerRepository.findById(id)
				.orElseThrow(() -> new OfferDoesNotExistException("Offer not found with id: " + id));

		validateRequest(request);

		String oldValue = existing.getTitle();

		existing.setTitle(request.getTitle().trim());
		existing.setDiscountType(request.getDiscountType());
		existing.setDiscountValue(request.getDiscountValue());
		existing.setStartDate(request.getStartDate());
		existing.setEndDate(request.getEndDate());

		Offer saved = offerRepository.save(existing);

		auditService.log(securityUtils.getCurrentUserId(), EntityName.OFFER, saved.getOfferId(),
				AuditAction.UPDATE_OFFER, oldValue, saved.getTitle());

		return OfferMapper.toResponseDTO(saved);
	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public void deactivate(int id) {
		Offer offer = offerRepository.findById(id)
				.orElseThrow(() -> new OfferDoesNotExistException("Offer not found with id: " + id));

		if (!offer.getIsActive()) {
			throw new BusinessException("Offer already inactive");
		}

		offer.setIsActive(false);

		offerRepository.save(offer);

		auditService.log(securityUtils.getCurrentUserId(), EntityName.OFFER, id, AuditAction.DEACTIVATE_OFFER, "true",
				"false");

	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public void activate(int id) {
		Offer offer = offerRepository.findById(id)
				.orElseThrow(() -> new OfferDoesNotExistException("Offer not found with id: " + id));

		if (offer.getIsActive()) {
			throw new BusinessException("Offer already active");
		}

		if (offer.getEndDate().isBefore(LocalDate.now())) {
			throw new BusinessException("Cannot activate expired offer");
		}

		offer.setIsActive(true);

		offerRepository.save(offer);

		auditService.log(securityUtils.getCurrentUserId(), EntityName.OFFER, id, AuditAction.ACTIVATE_OFFER, "false",
				"true");

	}

	@Override
	public List<OfferResponseDTO> getActiveOffers() {
		LocalDate today = LocalDate.now();

		return offerRepository.findByIsActiveTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqual(today, today)
				.stream().map(offer -> OfferMapper.toResponseDTO(offer)).collect(Collectors.toList());
	}

	@Override
	public OfferResponseDTO getActiveOfferById(int id) {
		Offer offer = offerRepository.findByOfferIdAndIsActiveTrue(id)
				.orElseThrow(() -> new OfferDoesNotExistException("Active offer not found with id: " + id));

		LocalDate today = LocalDate.now();

		if (offer.getStartDate().isAfter(today) || offer.getEndDate().isBefore(today)) {
			throw new BusinessException("Offer is not currently valid");
		}

		return OfferMapper.toResponseDTO(offer);
	}

	private void validateRequest(OfferRequestDTO request) {

		if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
			throw new BusinessException("Title is required");
		}

		if (request.getDiscountValue() == null || request.getDiscountValue().compareTo(BigDecimal.ZERO) <= 0) {
			throw new BusinessException("Discount must be greater than zero");
		}

		if (request.getStartDate() == null || request.getEndDate() == null) {
			throw new BusinessException("Start date and end date are required");
		}

		if (request.getStartDate().isAfter(request.getEndDate())) {
			throw new BusinessException("Start date cannot be after end date");
		}
	}

}