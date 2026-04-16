package com.mrs.enpoint.feature.user.controller;

import com.mrs.enpoint.feature.auth.dto.UserResponseDTO;
import com.mrs.enpoint.feature.user.dto.UserStatusRequestDTO;
import com.mrs.enpoint.feature.user.service.UserService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("https://localhost/5173")
@RestController
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
		return ResponseEntity.ok(userService.getAllUsers());
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserResponseDTO> getUserById(@PathVariable int id) {
		return ResponseEntity.ok(userService.getUserById(id));
	}

	@PatchMapping("/{id}/status")
	public ResponseEntity<UserResponseDTO> updateStatus(@PathVariable int id,
			@Valid @RequestBody UserStatusRequestDTO request) {
		return ResponseEntity.ok(userService.updateUserStatus(id, request));
	}
}