package com.koul.StudyCam.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LoginRequest {
	@NotBlank
	private String username;

	@NotBlank
	private String password;

	@Builder
	public LoginRequest(String username, String password) {
		this.username = username;
		this.password = password;
	}
}
