package com.koul.StudyCam.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.koul.StudyCam.user.dto.request.LoginRequest;
import com.koul.StudyCam.user.dto.request.SignupRequest;
import com.koul.StudyCam.user.dto.response.JwtResponse;
import com.koul.StudyCam.user.dto.response.MessageResponse;
import com.koul.StudyCam.user.service.AuthService;
import com.koul.StudyCam.user.utils.JwtUtils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

	private final AuthService authService;

	private final JwtUtils jwtUtils;

	@PostMapping("/sign-up")
	public ResponseEntity<MessageResponse> registerUser(@RequestBody SignupRequest signupRequest) {
		MessageResponse response = authService.resgisterUser(signupRequest);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/login")
	public ResponseEntity<JwtResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
		JwtResponse response = authService.authenticateUser(loginRequest);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/logout")
	public ResponseEntity<MessageResponse> logout(@RequestHeader("Authorization") String header) {
		if (header == null || !header.startsWith("Bearer ")) {
			return ResponseEntity.badRequest().body(new MessageResponse("Invalid token header"));
		}

		String token = header.substring(7);
		long expiration = jwtUtils.getExpiration(token);

		authService.addToBlacklist(token, expiration);

		return ResponseEntity.ok(new MessageResponse("success logout"));
	}
}
