package com.koul.StudyCam.User.service;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Optional;
import java.util.Set;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

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

class AuthServiceTest {

	@Mock
	private UserRepository userRepository;
	@Mock
	private RoleRepository roleRepository;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private AuthenticationManager authenticationManager;
	@Mock
	private JwtUtils jwtUtils;

	@InjectMocks
	private AuthService authService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void registerUser_success() {
		// given
		SignupRequest request = new SignupRequest();
		request.setUsername("john");
		request.setEmail("john@example.com");
		request.setPassword("123456");
		request.setRole(Set.of("user"));

		when(userRepository.existsByUsername("john")).thenReturn(false);
		when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
		when(passwordEncoder.encode("123456")).thenReturn("encodedPass");
		when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(new Role(1L, ERole.ROLE_USER)));

		// when
		MessageResponse response = authService.resgisterUser(request);

		// then
		assertThat(response.getMessage()).isEqualTo("User registered successfully");
		verify(userRepository, times(1)).save(any(User.class));
	}

	@Test
	void authenticateUser_success() {
		// given
		LoginRequest request = new LoginRequest();
		request.setUsername("john");
		request.setPassword("123456");

		Authentication authMock = mock(Authentication.class);
		UserDetailsImpl userDetails = new UserDetailsImpl(
			1L, "john", "john@example.com", "encodedPass", List.of(() -> "ROLE_USER")
		);

		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
			.thenReturn(authMock);
		when(authMock.getPrincipal()).thenReturn(userDetails);
		when(jwtUtils.generateJwtToken(authMock)).thenReturn("mockJwtToken");

		// when
		JwtResponse response = authService.authenticateUser(request);

		// then
		assertThat(response.getToken()).isEqualTo("mockJwtToken");
		assertThat(response.getUsername()).isEqualTo("john");
		assertThat(response.getRoles()).contains("ROLE_USER");
	}
}
