package com.koul.StudyCam.User.dto.request;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
public class SignupRequest {
	@NotBlank
	private String username;

	@NotBlank
	private String password;

	@NotBlank
	private String email;

	private Set<String> role;

	@Builder
	public SignupRequest(String username, String password, String email, Set<String> role) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.role = role;
	}
}
