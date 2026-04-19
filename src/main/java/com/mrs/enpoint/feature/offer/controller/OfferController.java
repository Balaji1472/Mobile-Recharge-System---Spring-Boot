package com.mrs.enpoint.feature.offer.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mrs.enpoint.feature.offer.dto.OfferRequestDTO;
import com.mrs.enpoint.feature.offer.dto.OfferResponseDTO;
import com.mrs.enpoint.feature.offer.service.OfferService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/offers")
public class OfferController {

	private final OfferService offerService;

	public OfferController(OfferService offerService) {
		this.offerService = offerService;
	}

	// create
	@PostMapping
	public ResponseEntity<OfferResponseDTO> createOffer(@Valid @RequestBody OfferRequestDTO request) {
		OfferResponseDTO response = offerService.createOffer(request);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	// get all
	@GetMapping
	public ResponseEntity<List<OfferResponseDTO>> getOffers(Authentication authentication) {
	    // Check if the user has Admin role
		boolean isAdmin = false;
	    
	    if (authentication != null && authentication.isAuthenticated()) {
	        isAdmin = authentication.getAuthorities().stream()
	                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
	    }

	    // Logic: Only Admin see all. Everyone else (Users & Guests) sees Active only.
	    if (isAdmin) {
	        return ResponseEntity.ok(offerService.getAllOffers());
	    } else {
	        return ResponseEntity.ok(offerService.getActiveOffers());
	    }
	}

	// get by id
	@GetMapping("/{id}")
	public ResponseEntity<OfferResponseDTO> getOfferById(@PathVariable int id) {
		return ResponseEntity.ok(offerService.getOfferById(id));
	}

	// update offer
	@PutMapping("/{id}")
	public ResponseEntity<OfferResponseDTO> updateOffer(@Valid @PathVariable int id, @RequestBody OfferRequestDTO request) {

		return ResponseEntity.ok(offerService.updateOffer(id, request));
	}

	// deactivate
	@PutMapping("/{id}/deactivate")
	public ResponseEntity<String> deactivate(@PathVariable int id) {
		offerService.deactivate(id);
		return ResponseEntity.ok("Offer deactivated successfully");
	}
 
	// activate
	@PutMapping("/{id}/activate")
	public ResponseEntity<String> activate(@PathVariable int id) {
		offerService.activate(id);
		return ResponseEntity.ok("Offer activated successfully");
	}

	// end offer
	@PutMapping("/{id}/end")
	public ResponseEntity<String> endOffer(@PathVariable int id) {
		offerService.deactivate(id); 
		return ResponseEntity.ok("Offer ended successfully");
	}
}
