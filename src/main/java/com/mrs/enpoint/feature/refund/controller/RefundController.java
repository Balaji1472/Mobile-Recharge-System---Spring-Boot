package com.mrs.enpoint.feature.refund.controller;

import com.mrs.enpoint.feature.refund.dto.RefundResponseDTO;
import com.mrs.enpoint.feature.refund.service.RefundService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/refunds")
public class RefundController {

    private final RefundService refundService;

    public RefundController(RefundService refundService) {
        this.refundService = refundService;
    }

    @GetMapping("/my")
    public ResponseEntity<List<RefundResponseDTO>> getMyRefunds() {
        return ResponseEntity.ok(refundService.getMyRefunds());
    }

    @GetMapping
    public ResponseEntity<List<RefundResponseDTO>> getAllRefunds() {
        return ResponseEntity.ok(refundService.getAllRefunds());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RefundResponseDTO> getRefundById(@PathVariable int id) {
        return ResponseEntity.ok(refundService.getRefundById(id));
    }
}