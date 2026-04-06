package com.mrs.enpoint.feature.savednumber.controller;

import com.mrs.enpoint.feature.savednumber.dto.SavedNumberRequestDTO;
import com.mrs.enpoint.feature.savednumber.dto.SavedNumberResponseDTO;
import com.mrs.enpoint.feature.savednumber.dto.SavedNumberUpdateDTO;
import com.mrs.enpoint.feature.savednumber.service.SavedNumberService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/saved-numbers")
public class SavedNumberController {

	private final SavedNumberService savedNumberService;

	public SavedNumberController(SavedNumberService savedNumberService) {
		this.savedNumberService = savedNumberService;
	}

	@GetMapping
	public ResponseEntity<List<SavedNumberResponseDTO>> getMySavedNumbers() {
		return ResponseEntity.ok(savedNumberService.getMySavedNumbers());
	}

	@PostMapping
	public ResponseEntity<SavedNumberResponseDTO> saveNumber(@Valid @RequestBody SavedNumberRequestDTO request) {
		return new ResponseEntity<>(savedNumberService.saveNumber(request), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<SavedNumberResponseDTO> updateNickname(@PathVariable int id,
			@Valid @RequestBody SavedNumberUpdateDTO request) {
		return ResponseEntity.ok(savedNumberService.updateNickname(id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteSavedNumber(@PathVariable int id) {
		savedNumberService.deleteSavedNumber(id);
		return ResponseEntity.ok("Saved number removed successfully");
	}
}