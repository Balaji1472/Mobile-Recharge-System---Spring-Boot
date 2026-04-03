package com.mrs.enpoint.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.mrs.enpoint.feature.auth.enums.RoleType;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "role")
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int roleId;

	@Enumerated(EnumType.STRING)
	@Column(name = "role_name", nullable = false, unique = true)
	private RoleType roleName;

	@NotBlank
	private String description;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
	private List<User> user = new ArrayList<>();

	public Role() {
	}

	public Role(RoleType roleName, @NotBlank String description, LocalDateTime createdAt) {
		this.roleName = roleName;
		this.description = description;
		this.createdAt = createdAt;
	}

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

	public List<User> getUser() {
		return user;
	}

	public void setUser(List<User> user) {
		this.user = user;
	}
}
