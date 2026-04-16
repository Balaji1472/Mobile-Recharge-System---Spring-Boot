package com.mrs.enpoint.feature.role.dto;

public class RoleCountDTO {

    private String roleName;
    private long count;

    public RoleCountDTO(String roleName, long count) {
        this.roleName = roleName;
        this.count = count;
    }

	public String getRoleName() {
        return roleName;
    }

    public long getCount() {
        return count;
    }
}