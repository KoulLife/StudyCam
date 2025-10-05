package com.koul.StudyCam.User.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.koul.StudyCam.User.domain.ERole;
import com.koul.StudyCam.User.domain.Role;
import com.koul.StudyCam.User.domain.User;
import com.koul.StudyCam.User.dto.request.LoginRequest;
import com.koul.StudyCam.User.dto.request.SignupRequest;
import com.koul.StudyCam.User.dto.response.JwtResponse;
import com.koul.StudyCam.User.dto.response.MessageResponse;
import com.koul.StudyCam.User.repository.RoleRepository;
import com.koul.StudyCam.User.repository.UserRepository;
import com.koul.StudyCam.User.utils.JwtUtils;
import com.koul.StudyCam.User.utils.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthService {

	private final PasswordEncoder encoder;

	private final UserRepository userRepository;

	private final RoleRepository roleRepository;

	private final AuthenticationManager authenticationManager;

	private final JwtUtils jwtUtils;

	@Transactional
	public MessageResponse resgisterUser(SignupRequest signupRequest) {
		if (userRepository.existsByUsername(signupRequest.getUsername())) {
			return new MessageResponse("Error: Username is already taken");
		}

		if (userRepository.existsByEmail(signupRequest.getEmail())) {
			return new MessageResponse("Error: Email is already in use");
		}

		User user = new User(
			signupRequest.getUsername(),
			encoder.encode(signupRequest.getPassword()),
			signupRequest.getEmail()
		);

		Set<Role> roles = assignRoles(signupRequest);
		user.updateRoles(roles);
		userRepository.save(user);

		return new MessageResponse("User registered successfully");
	}

	private Set<Role> assignRoles(SignupRequest signupRequest) {
		Set<String> strRoles = signupRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
				.orElseThrow(() -> new RuntimeException("Error: Role not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
					case "admin" -> {
						Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role not found."));
						roles.add(adminRole);
					}
					default -> {
						Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role not found."));
						roles.add(userRole);
					}
				}
			});
		}

		return roles;
	}

	public JwtResponse authenticateUser(LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
		);

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream()
			.map(item -> item.getAuthority())
			.collect(Collectors.toList());

		return new JwtResponse(
			jwt,
			userDetails.getId(),
			userDetails.getUsername(),
			userDetails.getEmail(),
			roles
		);
	}
}
