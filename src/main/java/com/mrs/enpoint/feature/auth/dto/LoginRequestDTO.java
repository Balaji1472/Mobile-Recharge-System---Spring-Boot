package com.mrs.enpoint.feature.auth.dto;

//import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.Pattern;

public class LoginRequestDTO {

	@NotBlank(message = "Email is required")
//	@Email(message = "Invalid email format")
	private String email;

	@NotBlank(message = "Password is required")
//	@Pattern(regexp = "^[A-Za-z\\d@$!%*?&#]{8,}$", message = "Password must be at least 8 characters long and contain both letters and numbers")
	private String password;

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
}
