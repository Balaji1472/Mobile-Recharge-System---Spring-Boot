package com.mrs.enpoint.feature.operator.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mrs.enpoint.feature.operator.dto.OperatorRequestDTO;
import com.mrs.enpoint.feature.operator.dto.OperatorResponseDTO;
import com.mrs.enpoint.feature.operator.service.OperatorService;

import jakarta.validation.Valid;

@CrossOrigin("http://localhost:5173/")
@RestController
@RequestMapping("/operators")
public class OperatorController {

    private final OperatorService operatorService;

    public OperatorController(OperatorService operatorService) {
        this.operatorService = operatorService;
    }

    @GetMapping
    public ResponseEntity<List<OperatorResponseDTO>> getAllOperators() {
        return ResponseEntity.ok(operatorService.getAllOperators());
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<OperatorResponseDTO>> getActiveOperators() {
        return ResponseEntity.ok(operatorService.getActiveOperators());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<OperatorResponseDTO> getOperatorById(@PathVariable int id) {
        return ResponseEntity.ok(operatorService.getOperatorById(id));
    }

    //create
    @PostMapping
    public ResponseEntity<OperatorResponseDTO> createOperator(@Valid @RequestBody OperatorRequestDTO request) {
        return new ResponseEntity<>(operatorService.createOperator(request), HttpStatus.CREATED);
    }

    //update
    @PutMapping("/{id}")
    public ResponseEntity<OperatorResponseDTO> updateOperator(@Valid @PathVariable int id, @RequestBody OperatorRequestDTO request) {
        return ResponseEntity.ok(operatorService.updateOperator(id, request));
    }

 // Activate Operator
    @PutMapping("/{id}/activate")
    public ResponseEntity<String> activateOperator(@PathVariable int id) {
        operatorService.activateOperator(id);
        return ResponseEntity.ok("Operator activated successfully");
    }

    // Deactivate Operator
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<String> deactivateOperator(@PathVariable int id) {
        operatorService.deactivateOperator(id);
        return ResponseEntity.ok("Operator deactivated successfully");
    }

}