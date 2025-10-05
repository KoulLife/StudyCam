package com.koul.StudyCam.User.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.koul.StudyCam.User.dto.request.LoginRequest;
import com.koul.StudyCam.User.dto.request.SignupRequest;
import com.koul.StudyCam.User.dto.response.JwtResponse;
import com.koul.StudyCam.User.dto.response.MessageResponse;
import com.koul.StudyCam.User.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

	private final AuthService authService;

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
}
