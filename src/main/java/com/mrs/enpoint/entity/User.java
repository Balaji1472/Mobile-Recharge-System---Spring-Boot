package com.mrs.enpoint.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import com.mrs.enpoint.feature.auth.enums.Gender;
import com.mrs.enpoint.feature.auth.enums.Status;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;

	@Column(name = "full_name", nullable = false)
	private String fullName;

	@Enumerated(EnumType.STRING)
	private Gender gender;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(name = "password_hash", nullable = false)
	private String password;

	@Column(name = "mobile_number", nullable = false, unique = true)
	private String mobileNumber;

	@Enumerated(EnumType.STRING)
	private Status status = Status.ACTIVE;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "role_id")
	private Role role;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	public User() {
	}

	public User(String fullName, Gender gender, String email, String password, String mobileNumber, Status status,
			Role role, LocalDateTime createdAt) {
		this.fullName = fullName;
		this.gender = gender;
		this.email = email;
		this.password = password;
		this.mobileNumber = mobileNumber;
		this.status = status;
		this.role = role;
		this.createdAt = createdAt;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}
