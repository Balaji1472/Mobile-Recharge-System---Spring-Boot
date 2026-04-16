package com.mrs.enpoint.feature.role.controller;

import com.mrs.enpoint.feature.role.dto.RoleCountDTO;
import com.mrs.enpoint.feature.role.dto.RoleResponseDTO;
import com.mrs.enpoint.feature.role.service.RoleService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<List<RoleResponseDTO>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> getRoleById(@PathVariable int id) {
        return ResponseEntity.ok(roleService.getRoleById(id));
    }
    
    @GetMapping("/counts")
    public ResponseEntity<List<RoleCountDTO>> getRoleCounts() {
        return ResponseEntity.ok(roleService.getRoleCounts());
    }
    
    
}