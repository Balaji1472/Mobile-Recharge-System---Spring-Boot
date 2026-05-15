package com.mrs.enpoint.feature.auditlog.controller;

//import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mrs.enpoint.feature.auditlog.dto.AuditResponseDTO;
import com.mrs.enpoint.feature.auditlog.service.AuditService;

@RestController
@RequestMapping("/audit-logs")
public class AuditController {

	private final AuditService auditService;

	public AuditController(AuditService authService) {
		this.auditService = authService;
	}

	@GetMapping
	public ResponseEntity<Page<AuditResponseDTO>> getAllLogs(
			@PageableDefault(size = 10, sort = "timestamp") Pageable pageable) {
		return ResponseEntity.ok(auditService.getAllLogs(pageable));
	}

	// get by id
	@GetMapping("/{id}")
	public ResponseEntity<AuditResponseDTO> getLogById(@PathVariable int id) {
		return ResponseEntity.ok(auditService.getLogById(id));
	}

}
