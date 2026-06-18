package com.task.dto;



import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {
	@NotBlank
	private String username;
	@Email
	@NotBlank
	private String email;
	@Size(min = 6, message = "Password must be at least 6 characters")
	private String password;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
	
	
	
}
