package com.mrs.enpoint.feature.role.dto;

import com.mrs.enpoint.feature.auth.enums.RoleType;

import java.time.LocalDateTime;

public class RoleResponseDTO {

    private int roleId;
    private RoleType roleName;
    private String description;
    private LocalDateTime createdAt;

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public RoleType getRoleName() {
        return roleName;
    }

    public void setRoleName(RoleType roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}