package com.mrs.enpoint.feature.auditlog.controller;

import java.util.List;

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
    
    //get all
    @GetMapping
    public ResponseEntity<List<AuditResponseDTO>> getAllLogs() {
        return ResponseEntity.ok(auditService.getAllLogs());
    } 

    //get by id
    @GetMapping("/{id}")
    public ResponseEntity<AuditResponseDTO> getLogById(@PathVariable int id) {
        return ResponseEntity.ok(auditService.getLogById(id));
    }
    
}
