package com.koul.StudyCam.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.koul.StudyCam.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	@EntityGraph(attributePaths = "roles")
	Optional<User> findByUsername(String username);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);
	
	Optional<User> findById(Long id);
}
